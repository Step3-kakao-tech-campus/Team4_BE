package com.ktc.matgpt.chatgpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.chatgpt.dto.FinishReason;
import com.ktc.matgpt.chatgpt.dto.GptRequest;
import com.ktc.matgpt.chatgpt.dto.GptRequestConverter;
import com.ktc.matgpt.chatgpt.dto.GptResponse;
import com.ktc.matgpt.chatgpt.entity.GptGuidance;
import com.ktc.matgpt.chatgpt.entity.GptReview;
import com.ktc.matgpt.chatgpt.exception.ApiException;
import com.ktc.matgpt.chatgpt.repository.GptGuidanceRepository;
import com.ktc.matgpt.chatgpt.repository.GptReviewRepository;
import com.ktc.matgpt.review.ReviewService;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.store.StoreService;
import com.ktc.matgpt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.ktc.matgpt.chatgpt.config.RestTemplateConfig.API_URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptService {

    private static final int REVIEW_COUNT = 10;

    private final GptGuidanceRepository gptGuidanceRepository;
    private final GptReviewRepository gptReviewRepository;
    private final UserService userService;
    private final ReviewService reviewService;
    private final StoreService storeService;
    private final RestTemplate restTemplate;


    @Async
    public void generateReviewSummarys(Long storeId) throws ExecutionException, InterruptedException {
        List<String> summaryTypes = List.of("BEST", "WORST");
        for (String summaryType : summaryTypes) {
            generateReviewSummary(storeId, summaryType);
        }
    }

    public void generateReviewSummary(Long storeId, String summaryType) throws ExecutionException, InterruptedException {
        List<Review> reviews = reviewService.findByStoreIdAndSummaryType(storeId, summaryType, REVIEW_COUNT);

        // TODO : 현재는 FakeDB로 연결되어 있어서 이 문장이 필요함.
        if (reviews.size() < 10) {
            return;
        }

        GptRequest requestBody = GptRequestConverter.convertFromReviews(reviews, summaryType);
        CompletableFuture<GptResponse> gptResponse = getGptResponse(requestBody);
        String reviewSummary = gptResponse.get().getContent();
        if (reviewSummary == null) {
            log.error("[ChatGPT API] : storeId-" + storeId + "리뷰 요약을 생성하는데 실패했습니다.");
            return;
        }

        updateOrCreateGptReview(storeId, summaryType, reviewSummary);
    }

    public String findReviewSummaryByStoreIdAndSummaryType(Long storeId, String summaryType) {
        summaryType = summaryType.toUpperCase();
        GptReview gptReview = gptReviewRepository.findByStoreIdAndSummaryType(storeId, summaryType).orElseThrow(
                () -> new NoSuchElementException("storeId-" + storeId + ": 해당 음식점에는 리뷰 요약이 존재하지 않습니다."));
        return gptReview.getContent();
    }

    @Transactional
    public String generateOrderGuidance(Long userId) throws ExecutionException, InterruptedException {

        // TODO : User로부터 국적(문화권) 받아오기
        // User user = userService.findById(userId);
        // String country = user.getCountry();

        String country = "india";
        GptRequest requestBody = GptRequestConverter.convertFromCountry(country);
        CompletableFuture<GptResponse> gptResponse = getGptResponse(requestBody);

        String content = gptResponse.get().getContent();
        if (content == null) {
            log.error("[ChatGPT API] : userId-" + userId + " 주문 가이드를 생성하는데 실패했습니다.");
            throw new ApiException.ContentNotFoundException();
        }

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

    public CompletableFuture<GptResponse> getGptResponse(GptRequest requestBody) {
        try {
            ResponseEntity<String> responseString = restTemplate.postForEntity(API_URL, requestBody, String.class);
            GptResponse response = new ObjectMapper().readValue(responseString.getBody(), GptResponse.class);
            validateResponse(response);
            return CompletableFuture.completedFuture(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) { // Http 상태 오류
            log.error("[ChatGPT API] HttpErrorException Occured : " + e.getStatusCode());
            throw new ApiException.UnknownFinishReasonException();
        } catch (ResourceAccessException e) { // 타임아웃 관련 오류
            log.error("[ChatGPT API] ResourceAccessException : " + e.getMessage());
            return CompletableFuture.completedFuture(getFallbackValue());
        } catch (RestClientException e) { // 기타 오류
            log.error("[ChatGPT API] RestClientException: " + e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw new ApiException.ContentNotFoundException();
        } catch (JsonProcessingException e) {
            log.error("[ChatGPT API] JsonProcessingException : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void updateOrCreateGptReview(Long storeId, String summaryType, String reviewSummary) {
        GptReview gptReview = findOrCreateGptReview(storeId, summaryType, reviewSummary);
        gptReviewRepository.save(gptReview);
        log.info("[ChatGPT API] : Store-" + storeId + " Review Summary Updated!");
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

    private static void validateResponse(GptResponse response) {
        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            log.error("[ChatGPT API] ContentNotFoundException occured: ");
            throw new ApiException.ContentNotFoundException();
        }

        if (FinishReason.TIMEOUT == response.getFinishReason()) {
            throw new ApiException.TimeoutException();
        }
        response.log();
    }

    private static GptResponse getFallbackValue() {
        List<GptResponse.Choice> choices = List.of(new GptResponse.Choice(FinishReason.TIMEOUT, 0, new GptResponse.Choice.Message(null, null)));
        return new GptResponse(0, choices, null, null, null, null);
    }
}
