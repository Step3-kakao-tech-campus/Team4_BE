package com.ktc.matgpt.feature_review.review;

import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import com.ktc.matgpt.feature_review.review.dto.ReviewResponse;
import com.ktc.matgpt.feature_review.s3.S3Service;
import com.ktc.matgpt.feature_review.store.MockStore;
import com.ktc.matgpt.feature_review.store.MockStoreService;
import com.ktc.matgpt.feature_review.utils.ApiUtils;
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
    private final MockStoreService mockStoreService;
    private final S3Service s3Service;

    @CrossOrigin
    @PostMapping("")        // request를 이미지 파일이 아닌 이미지 url으로 받음
    public ResponseEntity<?> create(@PathVariable(value = "storeId", required = true) Long storeId, @RequestBody ReviewRequest.CreateDTO requestDTO
            /*@RequestPart("key") ReviewRequest.CreateDTO requestDTO, @RequestPart(value = "file", required = false) MultipartFile file*/) {

        MockStore mockStore = mockStoreService.findById(storeId);
        Long createdId = reviewService.create(mockStore, requestDTO/*, file*/);
        String msg = "review-" + createdId + "(of store-" + storeId + ") created";

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(msg);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("")
    public ResponseEntity<?> findAllByStoreId(@PathVariable(value = "storeId", required = true) Long storeId) {

        List<ReviewResponse.FindAllByStoreIdDTO> responseDTOs = reviewService.findAllByStoreId(storeId);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> update(@PathVariable(value = "reviewId", required = true) Long reviewId,
                                    @RequestBody @Valid ReviewRequest.UpdateDTO requestDTO) {

        reviewService.update(reviewId, requestDTO);
        String msg = "review-" + reviewId + " updated";

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(msg);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> findById(@PathVariable(value = "reviewId", required = true) Long reviewId) {

        ReviewResponse.FindByReviewIdDTO responseDTO = reviewService.findByReviewId(reviewId);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(responseDTO);
        return ResponseEntity.ok(apiResult);
    }
}
