package com.ktc.matgpt.feature_review.review.mypage;

import com.ktc.matgpt.feature_review.review.ReviewService;
import com.ktc.matgpt.feature_review.review.dto.ReviewResponse;
import com.ktc.matgpt.feature_review.utils.ApiUtils;
import com.ktc.matgpt.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RequestMapping("/mypage")
@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final ReviewService reviewService;

    // 마이페이지 작성한 리뷰 조회
    @GetMapping("/my-reviews")
    public ResponseEntity<?> findAllByUserId(@RequestParam(defaultValue = "latest") String sortBy,
                                             @RequestParam(defaultValue = "1") int pageNum,
                                             @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ReviewResponse.FindPageByUserIdDTO responseDTOs =
                reviewService.findAllByUserId(userPrincipal.getId(), sortBy, pageNum);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }

}
