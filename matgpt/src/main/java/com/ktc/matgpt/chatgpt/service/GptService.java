package com.ktc.matgpt.chatgpt.service;

import com.ktc.matgpt.chatgpt.dto.FinishReason;
import com.ktc.matgpt.chatgpt.dto.GptRequest;
import com.ktc.matgpt.chatgpt.dto.GptRequestConverter;
import com.ktc.matgpt.chatgpt.dto.GptResponse;
import com.ktc.matgpt.chatgpt.entity.GptGuidance;
import com.ktc.matgpt.chatgpt.entity.GptReview;
import com.ktc.matgpt.chatgpt.exception.ApiException;
import com.ktc.matgpt.chatgpt.repository.GptGuidanceRepository;
import com.ktc.matgpt.chatgpt.repository.GptReviewRepository;
import com.ktc.matgpt.feature_review.review.ReviewService;
import com.ktc.matgpt.feature_review.review.entity.Review;
import com.ktc.matgpt.store.StoreService;
import com.ktc.matgpt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptService {

    public static final int API_RESPONSE_TIMEOUT_SECONDS = 20;
    public static final int REVIEW_COUNT = 10;

    private final GptGuidanceRepository gptGuidanceRepository;
    private final GptReviewRepository gptReviewRepository;
    private final UserService userService;
    private final ReviewService reviewService;
    private final StoreService storeService;
    private final WebClient webClient;

    @Transactional
    public void generateReviewSummary(Long storeId) {
        List<String> summaryTypes = List.of("BEST", "WORST");

        for (String summaryType: summaryTypes) {
            List<Review> reviews = reviewService.findByStoreIdAndSummaryType(storeId, summaryType, REVIEW_COUNT);

            if (reviews.size() < 10) {
                return;
            }

            String reviewSummary = getSummaryFromReviews(reviews);

            GptReview gptReview = findOrCreateGptReview(storeId, summaryType, reviewSummary);
            gptReviewRepository.save(gptReview);
        }
    }

    public String findReviewSummaryByStoreIdAndSummaryType(Long storeId, String summaryType) {
        summaryType = summaryType.toUpperCase();
        GptReview gptReview = gptReviewRepository.findByStoreIdAndSummaryType(storeId, summaryType).orElseThrow(
                () -> new NoSuchElementException("storeId-" + storeId + ": 해당 음식점에는 리뷰 요약이 존재하지 않습니다."));
        return gptReview.getContent();
    }

    @Transactional
    public String generateOrderGuidance(Long userId) {

        // TODO : User로부터 국적(문화권) 받아오기
        // User user = userService.findById(userId);
        // String country = user.getCountry();

        String country = "india";
        GptRequestConverter gptRequestConverter = new GptRequestConverter(country);
        String content = convertRequestToContent(gptRequestConverter);
        GptGuidance gptGuidance = GptGuidance.builder()
                .userId(userId)
                .content(content)
                .build();

        gptGuidanceRepository.save(gptGuidance);

        return content;
    }

    public int getLastNumsOfReview(Long storeId) {
        List<GptReview> gptReviews = gptReviewRepository.findByStoreId(storeId);
        if (gptReviews.isEmpty()) {
            return 0;
        }
        return gptReviews.get(0).getLastNumsOfReview();
    }

    // TODO : 비동기 처리 필요
    private String getSummaryFromReviews(List<Review> reviews) {
        GptRequestConverter gptRequestConverter = new GptRequestConverter(reviews);
        return convertRequestToContent(gptRequestConverter);
    }

    private GptReview findOrCreateGptReview(Long storeId, String summaryType, String content) {
        int currentNumsOfReview = storeService.getNumsOfReviewById(storeId);
        // ReviewSummary가 이미 존재한다면 수정, 존재하지 않는다면 생성
        return gptReviewRepository.findByStoreIdAndSummaryType(storeId, summaryType)
                .map(existingReview -> {
                    existingReview.updateContent(content);
                    existingReview.updateLastNumsOfReview(currentNumsOfReview);
                    return existingReview;
                })
                .orElseGet(() -> GptReview.builder()
                        .storeId(storeId)
                        .content(content)
                        .summaryType(summaryType)
                        .lastNumsOfReview(currentNumsOfReview)
                        .build());
    }

    private String convertRequestToContent(GptRequestConverter gptRequestConverter) {
        GptRequest requestBody = gptRequestConverter.convert();
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
