package com.ktc.matgpt.like.usecase;

import com.ktc.matgpt.like.likeReview.LikeReview;
import com.ktc.matgpt.like.likeReview.LikeReviewResponse;
import com.ktc.matgpt.like.likeReview.LikeReviewService;
import com.ktc.matgpt.review.ReviewService;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import com.ktc.matgpt.utils.PageResponse;
import com.ktc.matgpt.utils.Paging;
import jakarta.persistence.criteria.CriteriaBuilder;
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

    private final static int DEFAULT_PAGE_SIZE = 8;

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

    public PageResponse<?, LikeReviewResponse.FindLikeReviewPageDTO> executeFindLikeReviews(String userEmail, Long cursorId) {
        User userRef = userService.getReferenceByEmail(userEmail);
        cursorId = Paging.convertNullCursorToMaxValue(cursorId);
        List<LikeReview> likeReviews = likeReviewService.findReviewsByUserId(userRef.getId(), cursorId, DEFAULT_PAGE_SIZE+1);

        if (likeReviews.isEmpty()) {
            return new PageResponse<>(new Paging<>(false, 0, null, null), null);
        }

        Paging<Long> paging = getPagingInfo(likeReviews);
        List<LikeReviewResponse.FindLikeReviewPageDTO> reviewDTOs = new ArrayList<>();
        int count = 0;

        for (LikeReview likeReview : likeReviews) {
            if (++count > DEFAULT_PAGE_SIZE) break;
            Review review = likeReview.getReview();
            String relativeTime = reviewService.getRelativeTime(review.getCreatedAt());
            reviewDTOs.add(new LikeReviewResponse.FindLikeReviewPageDTO(review, relativeTime));
        }

        return new PageResponse<>(paging, reviewDTOs);
    }

    private Paging<Long> getPagingInfo(List<LikeReview> likeReviews) {
        boolean hasNext = false;
        int numsOfReviews = 0;

        if (likeReviews.size() == DEFAULT_PAGE_SIZE+1) {
            hasNext = true;
            numsOfReviews = DEFAULT_PAGE_SIZE;
        } else {
            numsOfReviews = likeReviews.size();
        }

        Long nextCursorId = likeReviews.get(numsOfReviews-1).getId();
        return new Paging<Long>(hasNext, numsOfReviews, nextCursorId, nextCursorId);
    }
}
