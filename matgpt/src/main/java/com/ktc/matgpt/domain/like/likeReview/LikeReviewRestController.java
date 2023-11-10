package com.ktc.matgpt.domain.like.likeReview;

import com.ktc.matgpt.domain.like.usecase.LikeReviewUseCase;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LikeReviewRestController {
    private final LikeReviewUseCase createLikeReviewUseCase;

    // 리뷰 좋아요 상태 전환하기 (좋아요 등록<->좋아요 취소)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("reviews/{reviewId}/like")
    public ResponseEntity<?> toggleLikeForReview(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boolean isLikeAdded = createLikeReviewUseCase.executeToggleLike(reviewId, userPrincipal.getEmail());

        String message = isLikeAdded ? "리뷰 좋아요 등록" : "리뷰 좋아요 취소";
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(message);

        return ResponseEntity.ok(apiResult);
    }

    // 좋아요를 누른 리뷰인지 확인
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/reviews/{reviewId}/if-liked")
    public ResponseEntity<?> checkIfLikedAlready(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boolean hasLikedAlready = createLikeReviewUseCase.isLikeAlreadyExists(reviewId, userPrincipal.getEmail());

        return ResponseEntity.ok(ApiUtils.success(new LikeReviewResponse.HasLikedDTO(hasLikedAlready)));
    }
}
