package com.ktc.matgpt.like.usecase;

import com.ktc.matgpt.like.likeReview.LikeReview;
import com.ktc.matgpt.like.likeReview.LikeReviewResponse;
import com.ktc.matgpt.like.likeReview.LikeReviewService;
import com.ktc.matgpt.review.ReviewService;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeReviewUseCase {
    private final UserService userService;
    private final LikeReviewService likeReviewService;
    private final ReviewService reviewService;

    @Transactional
    public boolean executeToggleLike(Long reviewId, String userEmail) {
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

    public LikeReviewResponse.FindLikeReviewPageDTO executeFindLikeReviews(String userEmail, Long cursorId) {
        User userRef = userService.getReferenceByEmail(userEmail);
        cursorId = Paging.convertNullCursorToMaxValue(cursorId);
        Page<LikeReview> likeReviews = likeReviewService.findReviewsByUserId(userRef.getId(), cursorId);

        List<LikeReviewResponse.FindLikeReviewPageDTO.ReviewDTO> reviewDTOs = new ArrayList<>();
        for (LikeReview likeReview : likeReviews) {
            Review review = likeReview.getReview();
            String relativeTime = reviewService.getRelativeTime(review.getCreatedAt());
            reviewDTOs.add(new LikeReviewResponse.FindLikeReviewPageDTO.ReviewDTO(review, likeReview.getUser(), relativeTime));
        }
        return new LikeReviewResponse.FindLikeReviewPageDTO(likeReviews, reviewDTOs);
    }
}
