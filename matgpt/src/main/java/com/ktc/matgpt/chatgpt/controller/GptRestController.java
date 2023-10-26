package com.ktc.matgpt.chatgpt.controller;

import com.ktc.matgpt.chatgpt.service.GptService;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gpt")
@RestController
public class GptRestController {

    private final GptService gptService;

    @PostMapping("/store/{storeId}/review")
    public ResponseEntity<?> generateReviewSummary(@PathVariable Long storeId) throws ExecutionException, InterruptedException {
        gptService.generateReviewSummarys(storeId);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success("success");
        return ResponseEntity.ok().body(apiResult);
    }

    @GetMapping("/store/{storeId}/review/{summaryType}")
    public ResponseEntity<?> getBestReviewSummary(@PathVariable(value="storeId") Long storeId,
                                                  @PathVariable(value="summaryType") String summaryType) {
        String content = gptService.findReviewSummaryByStoreIdAndSummaryType(storeId, summaryType);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(content);
        return ResponseEntity.ok().body(apiResult);
    }

    @PostMapping("/order")
    public ResponseEntity<?> generateOrderGuidance(/*@AuthenticationPrincipal UserPrincipal userPrincipal*/) throws ExecutionException, InterruptedException {
//        Long userId = userPrincipal.getId();
        Long userId = 1L;
        String content = gptService.generateOrderGuidance(userId);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(content);
        return ResponseEntity.ok().body(apiResult);
    }
}
