package com.ktc.matgpt.chatgpt.service;

import com.ktc.matgpt.chatgpt.dto.*;
import com.ktc.matgpt.chatgpt.exception.ApiException;
import com.ktc.matgpt.chatgpt.factory.MockReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptService {

    public static final int API_RESPONSE_TIMEOUT_SECONDS = 20;

    private final WebClient webClient;

    public String generateReviewSummary(List<MockReview> mockReviews) {
        GptRequest requestBody = ReviewsToGptRequestConverter.convert(mockReviews);
        GptResponse gptResponse = getGptResponse(requestBody);

        return gptResponse.getContent();
    }

    public String generateOrderPromptWithCountry(String country) {
        GptRequest requestBody = CountryToGptRequestConverter.convert(country);
        GptResponse gptResponse = getGptResponse(requestBody);

        return gptResponse.getContent();
    }

    private GptResponse getGptResponse(GptRequest requestBody) {
        GptResponse response = webClient
                .post()
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(GptResponse.class)
                .timeout(Duration.ofSeconds(API_RESPONSE_TIMEOUT_SECONDS))
                .onErrorReturn(TimeoutException.class, getFallbackValue())
                .block();

        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new ApiException.ContentNotFoundException();
        }

        if (FinishReason.TIMEOUT == response.getFinishReason()) {
            throw new ApiException.TimeoutException();
        }

        response.log();

        return response;
    }

    private static GptResponse getFallbackValue() {
        List<GptResponse.Choice> choices = List.of(new GptResponse.Choice(FinishReason.TIMEOUT, 0, null));
        return new GptResponse(0, choices, null);
    }

    private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
        log.info("[ChatGPT API] Error Occured : " + clientResponse.statusCode());
        return Mono.error(new ApiException.UnknownFinishReasonException());
    }
}
