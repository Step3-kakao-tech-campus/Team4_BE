package com.ktc.matgpt.chatgpt.controller;

import com.ktc.matgpt.chatgpt.annotation.Timer;
import com.ktc.matgpt.chatgpt.factory.MockReview;
import com.ktc.matgpt.chatgpt.factory.ReviewFactory;
import com.ktc.matgpt.chatgpt.service.GptService;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gpt")
@RestController
public class GptRestController {

    public static final int REVIEW_COUNT = 5;

    private final GptService gptService;

    @Timer
    @GetMapping("/review")
    public ResponseEntity<?> generateReviewSummary() {
        /** TODO
         * 0. Authorized된 사용자만이 접근 가능
         * 1. ReviewService에서 Review 받아오기
         * 2. 메서드 호출 방식을 Post로 변경
         */

        List<MockReview> mockReviews = ReviewFactory.getTopReviews(REVIEW_COUNT);
        return ResponseEntity.ok().body(ApiUtils.success(gptService.generateReviewSummary(mockReviews)));
    }

    @Timer
    @GetMapping("/order")
    public ResponseEntity<?> generateOrderPrompt() {
        /** TODO
         * 0. Authorized된 사용자만이 접근 가능
         * 1. 사용자 정보로부터 국적 받아오기
         * 2. 메서드 호출 방식을 Post로 변경
         */

        String country = "india";
        return ResponseEntity.ok().body(ApiUtils.success(gptService.generateOrderPromptWithCountry(country)));
    }
}
