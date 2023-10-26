package com.ktc.matgpt.review;

import com.ktc.matgpt.review.dto.ReviewRequest;
import com.ktc.matgpt.review.dto.ReviewResponse;
import com.ktc.matgpt.aws.S3Service;
import com.ktc.matgpt.feature_review.utils.ApiUtils;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.store.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/stores/{storeId}/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewRestController {
    private final ReviewService reviewService;
    private final StoreService storeService;
    private final S3Service s3Service;

    @CrossOrigin
    @PostMapping("")        // request를 이미지 파일이 아닌 이미지 url으로 받음
    public ResponseEntity<?> create(@PathVariable Long storeId, @RequestBody ReviewRequest.CreateDTO requestDTO, @AuthenticationPrincipal UserPrincipal userPrincipal
            /*@RequestPart("key") ReviewRequest.CreateDTO requestDTO, @RequestPart(value = "file", required = false) MultipartFile file*/) {
        ReviewResponse.UploadS3DTO uploadS3DTO = reviewService.createReview(userPrincipal.getId(), storeId, requestDTO);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(uploadS3DTO);
        return ResponseEntity.ok(apiResult);
    }

    // 음식점 리뷰 목록 조회
    @GetMapping("")
    public ResponseEntity<?> findAllByStoreId(@PathVariable Long storeId,
                                              @RequestParam(defaultValue = "latest") String sortBy,
                                              @RequestParam(defaultValue = "6") Long cursorId,
                                              @RequestParam(defaultValue = "5.0") double cursorRating
    ) {
        List<ReviewResponse.FindAllByStoreIdDTO> responseDTOs = reviewService.findAllByStoreId(storeId, sortBy, cursorId, cursorRating);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }



    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> update(@PathVariable Long reviewId,
                                    @RequestBody @Valid ReviewRequest.UpdateDTO requestDTO,
                                    @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        reviewService.update(reviewId, userPrincipal.getId(), requestDTO);
        String msg = "review-" + reviewId + " updated";

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(msg);
        return ResponseEntity.ok(apiResult);
    }

    // 개별 리뷰 상세조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> findById(@PathVariable Long reviewId) {
        ReviewResponse.FindByReviewIdDTO responseDTO = reviewService.findDetailByReviewId(reviewId);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(responseDTO);
        return ResponseEntity.ok(apiResult);
    }

    // TODO: s3 삭제 구현
    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reviewService.delete(reviewId, userPrincipal.getId());

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }
}
