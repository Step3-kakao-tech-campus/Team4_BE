package com.ktc.matgpt.review.mypage;

import com.ktc.matgpt.like.likeReview.LikeReviewResponse;
import com.ktc.matgpt.like.usecase.LikeReviewUseCase;
import com.ktc.matgpt.review.ReviewService;
import com.ktc.matgpt.review.dto.ReviewResponse;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value = "/mypage", produces = {"application/json; charset=UTF-8"})
@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final ReviewService reviewService;
    private final LikeReviewUseCase likeReviewUseCase;

    private static final String MAX_REVIEW_ID = "10000";
    private static final String MAX_LIKE_REVIEW_ID = "10000";
    private static final String MAX_LIKES_NUM = "10000";

    // 마이페이지 작성한 리뷰 조회
    @GetMapping("/my-reviews")
    public ResponseEntity<?> findAllByUserId(@RequestParam(defaultValue = "latest") String sortBy,
                                             @RequestParam(defaultValue = MAX_REVIEW_ID) Long cursorId,
                                             @RequestParam(defaultValue = MAX_LIKES_NUM) int cursorLikes,
                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PageResponse<?, ReviewResponse.FindPageByUserIdDTO> responseDTOs =
                reviewService.findAllByUserId(userPrincipal.getEmail(), sortBy, cursorId, cursorLikes);

        return ResponseEntity.ok(ApiUtils.success(responseDTOs));
    }

    // 마이페이지 좋아요한 리뷰 조회
    @GetMapping("/liked-reviews")
    public ResponseEntity<?> findLikedReviewsByUserId(@RequestParam(defaultValue = MAX_LIKE_REVIEW_ID) Long cursorId,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        LikeReviewResponse.FindLikeReviewPageDTO responseDTO
                = likeReviewUseCase.executeFindLikeReviews(userPrincipal.getEmail(), cursorId);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }
}
