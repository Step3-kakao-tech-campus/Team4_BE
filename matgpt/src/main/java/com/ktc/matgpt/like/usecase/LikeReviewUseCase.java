package com.ktc.matgpt.like.usecase;

import com.ktc.matgpt.image.ImageService;
import com.ktc.matgpt.like.likeReview.LikeReview;
import com.ktc.matgpt.like.likeReview.LikeReviewResponse;
import com.ktc.matgpt.like.likeReview.LikeReviewService;
import com.ktc.matgpt.review.ReviewService;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import com.ktc.matgpt.utils.CursorRequest;
import com.ktc.matgpt.utils.PageResponse;
import com.ktc.matgpt.utils.Paging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeReviewUseCase {
    private final UserService userService;
    private final LikeReviewService likeReviewService;
    private final ReviewService reviewService;
    private final ImageService imageService;

    private final static int DEFAULT_PAGE_SIZE = 8;
    private final static int DEFAULT_PAGE_SIZE_PLUS_ONE = DEFAULT_PAGE_SIZE + 1;
    private final static PageResponse EMPTY_PAGE_RESPONSE = new PageResponse<>(new Paging<>(false, 0, null, null), null);


    @Transactional
    public boolean executeToggleLike(Long reviewId, String userEmail) {
        User userRef = userService.getReferenceByEmail(userEmail);
        Review review = reviewService.findReviewByIdOrThrow(reviewId);

        boolean isLikeAdded = likeReviewService.toggleLikeForReview(userRef, review);
        if (isLikeAdded) review.plusRecommendCount();
        else review.minusRecommendCount();

        return isLikeAdded;
    }

    public PageResponse<?, LikeReviewResponse.LikeReviewDTO> executeFindLikeReviews(String userEmail, Long cursorId) {
        User userRef = userService.getReferenceByEmail(userEmail);
        CursorRequest<Long> page = new CursorRequest<>(DEFAULT_PAGE_SIZE_PLUS_ONE, cursorId, Long.class, cursorId);
        List<LikeReview> likeReviews = likeReviewService.findLikeReviewsByUserId(userRef.getId(), page);

        if (likeReviews.isEmpty()) return EMPTY_PAGE_RESPONSE;

        Paging paging = getPagingInfo(likeReviews);
        List<LikeReviewResponse.LikeReviewDTO> reviewDTOs = getLikeReviewDTOs(likeReviews.subList(0, paging.size()));
        return new PageResponse<>(paging, reviewDTOs);
    }

    private List<LikeReviewResponse.LikeReviewDTO> getLikeReviewDTOs(List<LikeReview> likeReviews) {
        return likeReviews.stream().map(likeReview -> {
            Review review = likeReview.getReview();
            String image = imageService.getFirstImageByReviewId(review.getId());
            String relativeTime = reviewService.getRelativeTime(review.getCreatedAt());
            return new LikeReviewResponse.LikeReviewDTO(review, relativeTime, image);
        }).collect(Collectors.toList());
    }


    private Paging<Long> getPagingInfo(List<LikeReview> likeReviews) {
        boolean hasNext = false;
        int numsOfReviews = likeReviews.size();

        if (numsOfReviews == DEFAULT_PAGE_SIZE_PLUS_ONE) {
            numsOfReviews--;
            hasNext = true;
        }

        Long nextCursorId = likeReviews.get(numsOfReviews-1).getId();
        return new Paging<Long>(hasNext, numsOfReviews, nextCursorId, nextCursorId);
    }
}
