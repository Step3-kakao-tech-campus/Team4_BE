package com.ktc.matgpt.review.mypage;

import com.ktc.matgpt.like.likeReview.LikeReviewResponseDTO;
import com.ktc.matgpt.like.likeReview.LikeReviewService;
import com.ktc.matgpt.review.ReviewService;
import com.ktc.matgpt.review.dto.ReviewResponse;
import com.ktc.matgpt.feature_review.utils.ApiUtils;
import com.ktc.matgpt.security.UserPrincipal;
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
    private final LikeReviewService likeReviewService;

    // 마이페이지 작성한 리뷰 조회
    @GetMapping("/my-reviews")
    public ResponseEntity<?> findAllByUserId(@RequestParam(defaultValue = "latest") String sortBy,
                                             @RequestParam(defaultValue = MAX_REVIEW_ID) Long cursorId,
                                             @RequestParam(defaultValue = MAX_LIKES_NUM) int cursorLikes,
                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ReviewResponse.FindPageByUserIdDTO responseDTOs =
                reviewService.findAllByUserId(userPrincipal.getId(), sortBy, cursorId, cursorLikes);

        return ResponseEntity.ok(ApiUtils.success(responseDTOs));
    }

    // 마이페이지 좋아요한 리뷰 조회
    @GetMapping("/liked-reviews")
    public ResponseEntity<?> findLikedReviewsByUserId(@RequestParam(defaultValue = "1") int pageNum,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        LikeReviewResponseDTO.FindLikeReviewsDTO responseDTO
                = likeReviewService.findReviewsByUserId(userPrincipal.getId(), pageNum);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }
}
