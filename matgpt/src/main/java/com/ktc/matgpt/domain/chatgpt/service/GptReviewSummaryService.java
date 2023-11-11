package com.ktc.matgpt.domain.chatgpt.service;

import com.ktc.matgpt.domain.chatgpt.dto.*;
import com.ktc.matgpt.domain.chatgpt.entity.GptReview;
import com.ktc.matgpt.domain.chatgpt.repository.GptReviewRepository;
import com.ktc.matgpt.domain.review.ReviewService;
import com.ktc.matgpt.domain.review.entity.Review;
import com.ktc.matgpt.domain.store.Store;
import com.ktc.matgpt.domain.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptReviewSummaryService {

    private final GptReviewRepository gptReviewRepository;
    private final ReviewService reviewService;
    private final StoreService storeService;
    private final GptApiService gptApiService;

    private static final int REVIEW_COUNT = 10;
    private static final List<String> summaryTypes = List.of("best", "worst");

    @Transactional
    public List<GptReviewSummaryResponse> generateReviewSummarys(Long storeId) {
        return summaryTypes.stream()
                .map(summaryType -> generateReviewSummary(storeId, summaryType))
                .toList();
    }

    @Transactional(readOnly = true)
    public GptReviewSummaryResponseDto<Map<String, String>> findReviewSummaryByStoreId(Long storeId) {
        Map<String, String> contents = new HashMap<>();
        for (String summaryType: summaryTypes) {
            GptReview gptReview = gptReviewRepository.findByStoreIdAndSummaryType(storeId, summaryType).orElse(null);
            if (gptReview == null) {
                return new GptReviewSummaryResponseDto<>(false, null);
            }
            contents.put(summaryType, gptReview.getContent());
        }
        return new GptReviewSummaryResponseDto<>(true, contents);
    }

    @Transactional(readOnly = true)
    public int getLastNumsOfReview(Long storeId) {
        List<GptReview> gptReviews = gptReviewRepository.findByStoreId(storeId);
        if (gptReviews.isEmpty()) {
            return 0;
        }
        return gptReviews.get(0).getLastNumsOfReview();
    }

    @Transactional
    public void updateOrCreateGptReview(Long storeId, String summaryType, String reviewSummary, LocalDateTime createdAt) {
        Store storeRef = storeService.getReferenceById(storeId);

        GptReview gptReview = findOrCreateGptReview(storeRef, summaryType, reviewSummary, createdAt);
        gptReviewRepository.save(gptReview);
        log.info("[ChatGPT API] : Store-{} Review Summary Updated!", storeRef.getId());
    }

    private GptReviewSummaryResponse generateReviewSummary(Long storeId, String summaryType) {
        List<Review> reviews = reviewService.findByStoreIdAndSummaryType(storeId, summaryType, REVIEW_COUNT);

        GptApiRequest requestBody = GptRequestConverter.convertFromReviewsAndSummaryType(reviews, summaryType);
        CompletableFuture<GptApiResponse> gptApiResponse = gptApiService.callChatGptApi(requestBody);
        return new GptReviewSummaryResponse(storeId, summaryType, gptApiResponse);
    }

    private GptReview findOrCreateGptReview(Store store, String summaryType, String content, LocalDateTime createdAt) {
        int currentNumsOfReview = storeService.getNumsOfReviewById(store.getId());
        // ReviewSummary가 이미 존재한다면 수정, 존재하지 않는다면 생성
        return gptReviewRepository.findByStoreIdAndSummaryType(store.getId(), summaryType)
                .map(existingReview -> {
                    existingReview.updateContent(content, createdAt);
                    existingReview.updateLastNumsOfReview(currentNumsOfReview);
                    return existingReview;
                })
                .orElseGet(() -> GptReview.builder()
                        .store(store)
                        .content(content)
                        .summaryType(summaryType)
                        .lastNumsOfReview(currentNumsOfReview)
                        .createdAt(createdAt)
                        .updatedAt(createdAt)
                        .build());
    }

}
