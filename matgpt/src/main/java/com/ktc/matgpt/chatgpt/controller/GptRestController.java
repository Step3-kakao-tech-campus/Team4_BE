package com.ktc.matgpt.chatgpt.controller;

import com.ktc.matgpt.chatgpt.annotation.Timer;
import com.ktc.matgpt.chatgpt.service.GptService;
import com.ktc.matgpt.feature_review.utils.ApiUtils;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gpt")
@RestController
public class GptRestController {

    private final GptService gptService;
    private final StoreService storeService;

    @Timer
    @PostMapping("/store/{storeId}/review")
    public ResponseEntity<?> generateReviewSummary(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @PathVariable Long storeId) {
        gptService.generateReviewSummary(storeId);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success("success");
        return ResponseEntity.ok().body(apiResult);
    }

    @GetMapping("/store/{storeId}/review/{summaryType}")
    public ResponseEntity<?> getBestReviewSummary(@PathVariable(value="storeId") Long storeId,
                                                  @PathVariable(value="summaryType") String summaryType) {
        String content = gptService.findReviewSummaryByStoreIdAndSummaryType(storeId, summaryType);
        return ResponseEntity.ok().body(ApiUtils.success(content));
    }

    @Timer
    @PostMapping("/order")
    public ResponseEntity<?> generateOrderGuidance(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        String content = gptService.generateOrderGuidance(userId);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(content);
        return ResponseEntity.ok().body(apiResult);
    }
}
