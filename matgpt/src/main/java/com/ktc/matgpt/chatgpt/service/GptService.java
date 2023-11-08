package com.ktc.matgpt.chatgpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.chatgpt.config.AsyncConfig;
import com.ktc.matgpt.chatgpt.dto.*;
import com.ktc.matgpt.chatgpt.entity.GptGuidance;
import com.ktc.matgpt.chatgpt.entity.GptReview;
import com.ktc.matgpt.chatgpt.exception.ApiException;
import com.ktc.matgpt.chatgpt.repository.GptGuidanceRepository;
import com.ktc.matgpt.chatgpt.repository.GptReviewRepository;
import com.ktc.matgpt.chatgpt.utils.UnixTimeConverter;
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

import java.time.LocalDateTime;
import java.util.*;
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
    private final AsyncConfig asyncConfig;

    private static final List<String> summaryTypes = List.of("best", "worst");

    public List<GptResponse> generateReviewSummarys(Long storeId) {
        return summaryTypes.stream()
                .map(summaryType -> generateReviewSummary(storeId, summaryType))
                .filter(Objects::nonNull)
                .toList();
    }

    public GptResponse generateReviewSummary(Long storeId, String summaryType) {
        List<Review> reviews = reviewService.findByStoreIdAndSummaryType(storeId, summaryType, REVIEW_COUNT);

        // TODO : 현재는 FakeDB로 연결되어 있어서 이 문장이 필요함. 추후 삭제 필요
        if (reviews.size() < 10) {
            return null;
        }

        GptApiRequest requestBody = GptRequestConverter.convertFromReviews(reviews, summaryType);
        CompletableFuture<GptApiResponse> gptApiResponse = callChatGptApi(requestBody);
        return new GptResponse(storeId, summaryType, gptApiResponse);
    }

    public GptResponseDto<Map<String, String>> findReviewSummaryByStoreId(Long storeId) {
        Map<String, String> contents = new HashMap<>();
        for (String summaryType: summaryTypes) {
            GptReview gptReview = gptReviewRepository.findByStoreIdAndSummaryType(storeId, summaryType).orElse(null);
            if (gptReview == null) {
                return new GptResponseDto<>(false, null);
            }
            contents.put(summaryType, gptReview.getContent());
        }
        return new GptResponseDto<>(true, contents);
    }

    @Transactional
    public String generateOrderGuidance(Long userId) throws ExecutionException, InterruptedException {

        // TODO : User로부터 국적(문화권) 받아오기
        // User user = userService.findById(userId);
        // String country = user.getCountry();

        String country = "india";
        GptApiRequest requestBody = GptRequestConverter.convertFromCountry(country);
        CompletableFuture<GptApiResponse> gptResponse = callChatGptApi(requestBody);

        String content = gptResponse.get().getContent();
        LocalDateTime createdAt = UnixTimeConverter.toLocalDateTime(gptResponse.get().created());
        if (content == null) {
            log.error("[ChatGPT API] : userId-" + userId + " 주문 가이드를 생성하는데 실패했습니다.");
            throw new ApiException.ContentNotFoundException();
        }

        GptGuidance gptGuidance = GptGuidance.builder()
                .userId(userId)
                .content(content)
                .createdAt(createdAt)
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

    @Transactional
    public void updateOrCreateGptReview(Long storeId, String summaryType, String reviewSummary, LocalDateTime createdAt) {
        GptReview gptReview = findOrCreateGptReview(storeId, summaryType, reviewSummary, createdAt);
        gptReviewRepository.save(gptReview);
        log.info("[ChatGPT API] : Store-{} Review Summary Updated!", storeId);
    }

    private GptReview findOrCreateGptReview(Long storeId, String summaryType, String content, LocalDateTime createdAt) {
        int currentNumsOfReview = storeService.getNumsOfReviewById(storeId);
        // ReviewSummary가 이미 존재한다면 수정, 존재하지 않는다면 생성
        return gptReviewRepository.findByStoreIdAndSummaryType(storeId, summaryType)
                .map(existingReview -> {
                    existingReview.updateContent(content, createdAt);
                    existingReview.updateLastNumsOfReview(currentNumsOfReview);
                    return existingReview;
                })
                .orElseGet(() -> GptReview.builder()
                        .storeId(storeId)
                        .content(content)
                        .summaryType(summaryType)
                        .lastNumsOfReview(currentNumsOfReview)
                        .createdAt(createdAt)
                        .updatedAt(createdAt)
                        .build());
    }

    private static void validateApiResponse(GptApiResponse apiResponse) {
        if (apiResponse == null || apiResponse.choices() == null || apiResponse.choices().isEmpty()) {
            log.error("[ChatGPT API] ContentNotFoundException occured.");
            throw new ApiException.ContentNotFoundException();
        }

        if (FinishReason.TIMEOUT == apiResponse.getFinishReason()) {
            throw new ApiException.TimeoutException();
        }
        apiResponse.log();
    }

    private static GptApiResponse getFallbackValue() {
        List<GptApiResponse.Choice> choices = List.of(new GptApiResponse.Choice(FinishReason.TIMEOUT, 0, new GptApiResponse.Choice.Message(null, null)));
        return new GptApiResponse(0, choices, null, null, null, null);
    }
}
