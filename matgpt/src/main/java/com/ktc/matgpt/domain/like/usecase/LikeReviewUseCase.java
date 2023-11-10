package com.ktc.matgpt.domain.like.usecase;

import com.ktc.matgpt.domain.image.ImageService;
import com.ktc.matgpt.domain.like.likeReview.LikeReviewResponse;
import com.ktc.matgpt.domain.like.likeReview.LikeReview;
import com.ktc.matgpt.domain.like.likeReview.LikeReviewService;
import com.ktc.matgpt.domain.review.ReviewService;
import com.ktc.matgpt.domain.review.entity.Review;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.domain.user.service.UserService;
import com.ktc.matgpt.utils.paging.CursorRequest;
import com.ktc.matgpt.utils.paging.PageResponse;
import com.ktc.matgpt.utils.paging.Paging;
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public boolean isLikeAlreadyExists(Long reviewId, String userEmail) {
        User userRef = userService.getReferenceByEmail(userEmail);
        Review reviewRef = reviewService.getReferenceById(reviewId);
        return likeReviewService.isLikeAlreadyExists(userRef, reviewRef);
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
