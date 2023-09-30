package com.ktc.matgpt.chatgpt.controller;

import com.ktc.matgpt.chatgpt.annotation.Timer;
import com.ktc.matgpt.chatgpt.factory.Review;
import com.ktc.matgpt.chatgpt.factory.ReviewFactory;
import com.ktc.matgpt.chatgpt.service.GptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GptController {

    public static final int REVIEW_COUNT = 5;

    private final GptService gptService;

    @Timer
    @GetMapping("/gpt/review")
    public ResponseEntity<?> generateReviewSummary() throws TimeoutException {
        List<Review> reviews = ReviewFactory.getTopReviews(REVIEW_COUNT);
        return ResponseEntity.ok().body(gptService.generateReviewSummary(reviews));
    }
}