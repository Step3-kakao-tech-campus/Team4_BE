package com.ktc.matgpt.like.usecase;

import com.ktc.matgpt.like.likeReview.LikeReviewService;
import com.ktc.matgpt.review.ReviewService;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateLikeReviewUseCase {
    private final UserService userService;
    private final LikeReviewService likeReviewService;
    private final ReviewService reviewService;

    @Transactional
    public boolean execute(Long reviewId, String userEmail) {
        User userRef = userService.getReferenceByEmail(userEmail);
        Review review = reviewService.findReviewByIdOrThrow(reviewId);

        boolean isLikeAdded = likeReviewService.toggleLikeForReview(userRef, review);
        if (isLikeAdded) {
            review.plusRecommendCount();
        }
        else {
            review.minusRecommendCount();
        }
        return isLikeAdded;
    }
}
