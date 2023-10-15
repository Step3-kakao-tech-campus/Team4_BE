package com.ktc.matgpt.feature_review.review;

import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import com.ktc.matgpt.feature_review.review.dto.ReviewResponse;
import com.ktc.matgpt.feature_review.s3.S3Service;
import com.ktc.matgpt.feature_review.utils.ApiUtils;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> create(@PathVariable Long storeId, @RequestBody ReviewRequest.CreateDTO requestDTO
            /*@RequestPart("key") ReviewRequest.CreateDTO requestDTO, @RequestPart(value = "file", required = false) MultipartFile file*/) {

        Store store = storeService.findEntityById(storeId);
        Long createdId = reviewService.create(store, requestDTO/*, file*/);
        String msg = "review-" + createdId + "(of store-" + storeId + ") created";

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(msg);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("")
    public ResponseEntity<?> findAllByStoreId(@PathVariable Long storeId) {

        List<ReviewResponse.FindAllByStoreIdDTO> responseDTOs = reviewService.findAllByStoreId(storeId);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> update(@PathVariable Long reviewId,
                                    @RequestBody @Valid ReviewRequest.UpdateDTO requestDTO) {

        reviewService.update(reviewId, requestDTO);
        String msg = "review-" + reviewId + " updated";

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(msg);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> findById(@PathVariable Long reviewId) {

        ReviewResponse.FindByReviewIdDTO responseDTO = reviewService.findByReviewId(reviewId);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(responseDTO);
        return ResponseEntity.ok(apiResult);
    }

    // 리뷰 삭제 (s3삭제 구현 필요)
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reviewService.delete(reviewId, userPrincipal.getId());

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }
}
