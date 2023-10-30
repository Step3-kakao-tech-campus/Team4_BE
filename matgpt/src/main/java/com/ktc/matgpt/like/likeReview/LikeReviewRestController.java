package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.like.likeStore.LikeStoreService;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LikeReviewRestController {
    private final LikeReviewService likeReviewService;

    @PostMapping("reviews/{reviewId}/like")
    public ResponseEntity<?> toggleLikeForReview(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boolean isLikeAdded = likeReviewService.toggleLikeForReview(reviewId, userPrincipal.getEmail());

        String message = isLikeAdded ? "즐겨찾기 성공" : "즐겨찾기 취소 성공";
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(message);

        return ResponseEntity.ok(apiResult);
    }
}
