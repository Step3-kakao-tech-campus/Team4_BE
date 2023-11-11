package com.ktc.matgpt.domain.review.mypage;

import com.ktc.matgpt.domain.like.likeReview.LikeReviewResponse;
import com.ktc.matgpt.domain.like.usecase.LikeReviewUseCase;
import com.ktc.matgpt.domain.review.ReviewService;
import com.ktc.matgpt.domain.review.dto.ReviewResponse;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import com.ktc.matgpt.utils.paging.PageResponse;
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

    // 마이페이지 작성한 리뷰 조회
    @GetMapping("/my-reviews")
    public ResponseEntity<?> findAllByUserId(@RequestParam(defaultValue = "latest") String sortBy,
                                             @RequestParam(required = false) Long cursorId,
                                             @RequestParam(required = false) Integer cursor,
                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PageResponse<?, ReviewResponse.UserReviewDTO> responseDTOs =
                reviewService.findPageByUserId(userPrincipal.getEmail(), sortBy, cursorId, cursor);

        return ResponseEntity.ok(ApiUtils.success(responseDTOs));
    }

    // 마이페이지 좋아요한 리뷰 조회
    @GetMapping("/liked-reviews")
    public ResponseEntity<?> findLikedReviewsByUserId(@RequestParam(required = false) Long cursorId,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PageResponse<?, LikeReviewResponse.LikeReviewDTO>  responseDTO
                            = likeReviewUseCase.findLikeReviews(userPrincipal.getEmail(), cursorId);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }
}
