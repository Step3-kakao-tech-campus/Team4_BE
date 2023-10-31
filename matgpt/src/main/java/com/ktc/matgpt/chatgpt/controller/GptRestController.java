package com.ktc.matgpt.chatgpt.controller;

import com.ktc.matgpt.chatgpt.dto.GptApiResponse;
import com.ktc.matgpt.chatgpt.dto.GptResponse;
import com.ktc.matgpt.chatgpt.dto.GptResponseDto;
import com.ktc.matgpt.chatgpt.service.GptService;
import com.ktc.matgpt.chatgpt.utils.UnixTimeConverter;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    private final GptService gptService;

    @GetMapping("/store/{storeId}/review")
    public ResponseEntity<?> getReviewSummarys(@PathVariable(value = "storeId") Long storeId) {
        GptResponseDto<Map<String, String>> gptResponseDto = gptService.findReviewSummaryByStoreId(storeId);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(gptResponseDto);
        return ResponseEntity.ok().body(apiResult);
    }

    @PostMapping("/order")
    public ResponseEntity<?> generateOrderGuidance(@AuthenticationPrincipal UserPrincipal userPrincipal) throws ExecutionException, InterruptedException {
        String content = gptService.generateOrderGuidance(userPrincipal.getId());
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(content);
        return ResponseEntity.ok().body(apiResult);
    }

    // 테스트 전용 API 입니다. 실제로 이 API는 사용되지 않습니다.
    @PostMapping("/store/{storeId}/review")
    public ResponseEntity<?> generateReviewSummary(@PathVariable Long storeId) {
        List<GptResponse> gptResponses = gptService.generateReviewSummarys(storeId);
        gptResponses.forEach(gptResponse -> {
            GptApiResponse gptApiResponse = gptResponse.gptApiResponse().join();

            if (gptApiResponse == null) {
                log.error("[ChatGPT API] : storeId-{} GptApiResponse : null", gptResponse.storeId());
                return;
            }

            String reviewSummary = gptApiResponse.getContent();
            LocalDateTime createdAt = UnixTimeConverter.toLocalDateTime(gptApiResponse.created());

            if (reviewSummary == null) {
                log.error("[ChatGPT API] : storeId-{} 리뷰 요약을 생성하는데 실패했습니다.", gptResponse.storeId());
                return;
            }

            gptService.updateOrCreateGptReview(gptResponse.storeId(), gptResponse.summaryType(), reviewSummary, createdAt);
        });

        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success("success");
        return ResponseEntity.ok().body(apiResult);
    }
}
