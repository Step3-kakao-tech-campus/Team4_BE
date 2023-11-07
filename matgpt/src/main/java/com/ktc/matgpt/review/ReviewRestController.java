package com.ktc.matgpt.review;

import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;
import com.ktc.matgpt.review.dto.ReviewRequest;
import com.ktc.matgpt.review.dto.ReviewResponse;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/stores/{storeId}/reviews", produces = {"application/json; charset=UTF-8"})
@RequiredArgsConstructor
@RestController
public class ReviewRestController {
    private final ReviewService reviewService;

    private static final String MAX_REVIEW_ID = "10000";
    private static final String MAX_LIKES_NUM = "10000";

    // 첫 번째 단계: 리뷰 임시 저장 및 Presigned URL 반환
    @PostMapping(value = "/temp")
    public ResponseEntity<?> createTemporaryReview(@PathVariable Long storeId,
                                                   @RequestBody ReviewRequest.SimpleCreateDTO requestDTO,
                                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            //파일 검증 로직 삭제
            Review review = reviewService.createTemporaryReview(userPrincipal.getEmail(), storeId, requestDTO);
            List<ReviewResponse.UploadS3DTO.PresignedUrlDTO> presignedUrls = reviewService.createPresignedUrls(review.getReviewUuid(), requestDTO.getImageCount());
            return ResponseEntity.ok(ApiUtils.success(new ReviewResponse.UploadS3DTO(review.getId(), presignedUrls)));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REVIEW_PROCESS_ERROR);
        }
    }


    // 두 번째 단계: 이미지와 태그 정보를 포함하여 리뷰 완료
    @PostMapping("/{reviewId}")
    public ResponseEntity<?> completeReview(@PathVariable Long storeId,
                                            @PathVariable Long reviewId,
                                            @RequestBody ReviewRequest.CreateCompleteDTO requestDTO) {
        try {
            reviewService.completeReviewUpload(storeId, reviewId, requestDTO);
            return ResponseEntity.ok(ApiUtils.success("리뷰가 성공적으로 완료되었습니다."));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REVIEW_PROCESS_ERROR);
        }
    }

    // 음식점 리뷰 목록 조회
    @GetMapping("")
    public ResponseEntity<?> findAllByStoreId(@PathVariable Long storeId,
                                              @RequestParam(defaultValue = "latest") String sortBy,
                                              @RequestParam(defaultValue = MAX_REVIEW_ID) Long cursorId,
                                              @RequestParam(defaultValue = MAX_LIKES_NUM) int cursorLikes
    ) {
        ReviewResponse.FindPageByStoreIdDTO responseDTO = reviewService.findAllByStoreId(storeId, sortBy, cursorId, cursorLikes);
        return ResponseEntity.ok(com.ktc.matgpt.utils.ApiUtils.success(responseDTO));
    }


    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> update(@PathVariable Long storeId,
                                    @PathVariable Long reviewId,
                                    @RequestBody @Valid ReviewRequest.UpdateDTO requestDTO,
                                    @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        reviewService.updateContent(reviewId, userPrincipal.getEmail(), requestDTO);
        return ResponseEntity.ok(ApiUtils.success("리뷰 내용이 수정되었습니다."));
    }

    // 개별 리뷰 상세조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> findById(@PathVariable Long storeId,
                                      @PathVariable Long reviewId) {
        ReviewResponse.FindByReviewIdDTO responseDTO = reviewService.findDetailByReviewId(reviewId);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }

    // TODO: s3 삭제 구현
    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reviewService.delete(reviewId, userPrincipal.getEmail());
        return ResponseEntity.ok(ApiUtils.success("리뷰가 삭제되었습니다."));
    }
}
