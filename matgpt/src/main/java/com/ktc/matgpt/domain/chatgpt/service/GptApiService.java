package com.ktc.matgpt.domain.chatgpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.config.AsyncConfig;
import com.ktc.matgpt.domain.chatgpt.dto.FinishReason;
import com.ktc.matgpt.domain.chatgpt.dto.GptApiRequest;
import com.ktc.matgpt.domain.chatgpt.dto.GptApiResponse;
import com.ktc.matgpt.exception.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.ktc.matgpt.config.RestTemplateConfig.API_URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptApiService {

    private final RestTemplate restTemplate;
    private final AsyncConfig asyncConfig;

    @Async
    public CompletableFuture<GptApiResponse> callChatGptApi(GptApiRequest requestBody) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<String> responseString = restTemplate.postForEntity(API_URL, requestBody, String.class);
                GptApiResponse apiResponse = new ObjectMapper().readValue(responseString.getBody(), GptApiResponse.class);
                validateApiResponse(apiResponse);
                return apiResponse;

            } catch (HttpClientErrorException | HttpServerErrorException e) { // Http 상태 오류
                log.error("[ChatGPT API] HttpErrorException Occured : " + e.getStatusCode());
                throw new ApiException.UnknownFinishReasonException();

            } catch (ResourceAccessException e) { // 타임아웃 관련 오류
                log.error("[ChatGPT API] ResourceAccessException : " + e.getMessage());
                return getFallbackValue();

            } catch (RestClientException e) { // 기타 오류
                log.error("[ChatGPT API] RestClientException: " + e.getMessage());
                log.error(Arrays.toString(e.getStackTrace()));
                throw new ApiException.ContentNotFoundException();

            } catch (JsonProcessingException e) {
                log.error("[ChatGPT API] JsonProcessingException : " + e.getMessage());
                throw new RuntimeException(e);
            }
        }, asyncConfig.getAsyncExecutor()); // 사용자 정의 스레드 풀 사용
    }

    private static void validateApiResponse(GptApiResponse apiResponse) {
        if (apiResponse == null || apiResponse.choices() == null || apiResponse.choices().isEmpty() || apiResponse.getContent() == null) {
            log.error("[ChatGPT API] ContentNotFoundException occured.");
            throw new ApiException.ContentNotFoundException();
        }

        if (FinishReason.TIMEOUT == apiResponse.getFinishReason()) {
            log.error("[ChatGPT API] TimeoutException occured.");
            throw new ApiException.TimeoutException();
        }
        apiResponse.log();
    }

    private static GptApiResponse getFallbackValue() {
        List<GptApiResponse.Choice> choices = List.of(new GptApiResponse.Choice(FinishReason.TIMEOUT, 0, new GptApiResponse.Choice.Message(null, null)));
        return new GptApiResponse(0, choices, null, null, null, null);
    }
}
