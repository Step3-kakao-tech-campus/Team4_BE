package com.ktc.matgpt.domain.chatgpt.controller;

import com.ktc.matgpt.domain.chatgpt.annotation.Timer;
import com.ktc.matgpt.domain.chatgpt.dto.GptApiResponse;
import com.ktc.matgpt.domain.chatgpt.dto.GptOrderGuidanceResponseDto;
import com.ktc.matgpt.domain.chatgpt.dto.GptReviewSummaryResponse;
import com.ktc.matgpt.domain.chatgpt.dto.GptReviewSummaryResponseDto;
import com.ktc.matgpt.domain.chatgpt.service.GptOrderGuidanceService;
import com.ktc.matgpt.domain.chatgpt.service.GptReviewSummaryService;
import com.ktc.matgpt.domain.chatgpt.utils.UnixTimeConverter;
import com.ktc.matgpt.domain.store.Store;
import com.ktc.matgpt.domain.store.StoreService;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gpt")
@RestController
public class GptRestController {

    private final StoreService storeService;
    private final GptOrderGuidanceService gptOrderGuidanceService;
    private final GptReviewSummaryService gptReviewSummaryService;

    @GetMapping("/stores/{storeId}/review")
    public ResponseEntity<?> getReviewSummarys(@PathVariable Long storeId) {
        GptReviewSummaryResponseDto<Map<String, String>> gptReviewSummaryResponseDto = gptReviewSummaryService.findReviewSummaryByStoreId(storeId);
        return ResponseEntity.ok().body(ApiUtils.success(gptReviewSummaryResponseDto));
    }

    @Timer
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/order")
    public ResponseEntity<?> generateOrderGuidance(@AuthenticationPrincipal UserPrincipal userPrincipal) throws ExecutionException, InterruptedException {
        String content = gptOrderGuidanceService.generateOrderGuidance(userPrincipal.getId());
        return ResponseEntity.ok().body(ApiUtils.success(content));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/order")
    public ResponseEntity<?> getOrderGuidance(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<GptOrderGuidanceResponseDto> GptOrderGuidanceResponseDtos = gptOrderGuidanceService.getOrderGuidances(userPrincipal.getId());
        return ResponseEntity.ok().body(ApiUtils.success(GptOrderGuidanceResponseDtos));
    }

    // 테스트 전용 API 입니다. 실제로 이 API는 사용되지 않습니다. 리뷰 요약 동작은 Cron을 이용해서만 동작합니다.
    @Timer
    @Profile("dev")
    @PostMapping("/stores/{storeId}/review")
    public ResponseEntity<?> generateReviewSummary(@PathVariable Long storeId) {
        Store store = storeService.findById(storeId);
        List<GptReviewSummaryResponse> gptReviewSummaryResponses = gptReviewSummaryService.generateReviewSummarys(store.getId());
        gptReviewSummaryResponses.forEach(gptReviewSummaryResponse -> {
            GptApiResponse gptApiResponse = gptReviewSummaryResponse.gptApiResponse().join();

            if (gptApiResponse == null) {
                log.error("[ChatGPT API] : storeId-{} GptApiResponse : null", gptReviewSummaryResponse.storeId());
                return;
            }

            String reviewSummary = gptApiResponse.getContent();
            LocalDateTime createdAt = UnixTimeConverter.toLocalDateTime(gptApiResponse.created());

            if (reviewSummary == null) {
                log.error("[ChatGPT API] : storeId-{} 리뷰 요약을 생성하는데 실패했습니다.", gptReviewSummaryResponse.storeId());
                return;
            }

            gptReviewSummaryService.updateOrCreateGptReview(gptReviewSummaryResponse.storeId(), gptReviewSummaryResponse.summaryType(), reviewSummary, createdAt);
        });

        return ResponseEntity.ok().body(ApiUtils.success("success"));
    }
}
