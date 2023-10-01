package com.ktc.matgpt.chatgpt.service;

import com.ktc.matgpt.chatgpt.dto.FinishReason;
import com.ktc.matgpt.chatgpt.dto.GptRequest;
import com.ktc.matgpt.chatgpt.dto.GptResponse;
import com.ktc.matgpt.chatgpt.dto.ReviewsToGptRequestConverter;
import com.ktc.matgpt.chatgpt.factory.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptService {

    public static final int API_RESPONSE_TIMEOUT_SECONDS = 10;

    private final WebClient webClient;

    public String generateReviewSummary(List<Review> reviews) throws TimeoutException {
        GptRequest requestBody = ReviewsToGptRequestConverter.convert(reviews);
        GptResponse gptResponse = getGptResponse(requestBody);

        return gptResponse.getContent();
    }

    private GptResponse getGptResponse(GptRequest requestBody) throws TimeoutException {
        GptResponse response = webClient
                .post()
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(GptResponse.class)
                .timeout(Duration.ofSeconds(API_RESPONSE_TIMEOUT_SECONDS))
                .onErrorReturn(TimeoutException.class, getFallbackValue())
                .block();

        Objects.requireNonNull(response, "[ChatGPT API] No Response");
        response.validate();
        response.log();

        return response;
    }

    private static GptResponse getFallbackValue() {
        List<GptResponse.Choice> choices = List.of(new GptResponse.Choice(FinishReason.TIMEOUT, 0, null));
        return new GptResponse(0, choices, null);
    }

    private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
        return Mono.error(new RuntimeException("[ChatGPT API] Error Occured : " + clientResponse.statusCode()));
    }
}
