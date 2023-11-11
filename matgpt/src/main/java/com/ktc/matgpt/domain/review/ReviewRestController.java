package com.ktc.matgpt.domain.review;

import com.ktc.matgpt.domain.review.dto.ReviewResponse;
import com.ktc.matgpt.domain.review.dto.ReviewRequest;
import com.ktc.matgpt.domain.review.entity.Review;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import com.ktc.matgpt.utils.paging.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/stores/{storeId}/reviews", produces = {"application/json; charset=UTF-8"})
@RequiredArgsConstructor
@RestController
public class ReviewRestController {
    private final ReviewService reviewService;

    // 첫 번째 단계: 리뷰 임시 저장 및 Presigned URL 반환
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/temp")
    public ResponseEntity<?> createTemporaryReview(@PathVariable Long storeId,
                                                   @RequestBody ReviewRequest.SimpleCreateDTO requestDTO,
                                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {
        //파일 검증 로직 삭제
        Review review = reviewService.createTemporaryReview(userPrincipal.getEmail(), storeId, requestDTO);
        List<ReviewResponse.UploadS3DTO.PresignedUrlDTO> presignedUrls = reviewService.createPresignedUrls(review.getReviewUuid(), requestDTO.getImageCount());
        return ResponseEntity.ok(ApiUtils.success(new ReviewResponse.UploadS3DTO(review.getId(), presignedUrls)));
    }


    // 두 번째 단계: 이미지와 태그 정보를 포함하여 리뷰 완료
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{reviewId}")
    public ResponseEntity<?> completeReview(@PathVariable Long storeId,
                                            @PathVariable Long reviewId,
                                            @RequestBody ReviewRequest.CreateCompleteDTO requestDTO,
                                            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        reviewService.completeReviewUpload(storeId, reviewId, requestDTO, userPrincipal.getEmail());
        return ResponseEntity.ok(ApiUtils.success("리뷰가 성공적으로 완료되었습니다."));
    }

    // 음식점 리뷰 목록 조회
    @GetMapping("")
    public ResponseEntity<?> findAllByStoreId(@PathVariable Long storeId,
                                              @RequestParam(defaultValue = "latest") String sortBy,
                                              @RequestParam(required = false) Long cursorId,
                                              @RequestParam(required = false) Integer cursor
    ) {
        PageResponse<?, ReviewResponse.StoreReviewDTO> responseDTO = reviewService.findPageByStoreId(storeId, sortBy, cursorId, cursor);
        return ResponseEntity.ok(com.ktc.matgpt.utils.ApiUtils.success(responseDTO));
    }


    // 리뷰 수정
    @PreAuthorize("isAuthenticated()")
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
                                      @PathVariable Long reviewId,
                                      @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ReviewResponse.FindByReviewIdDTO responseDTO = reviewService.findDetailByReviewId(reviewId, userPrincipal);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }

    // 리뷰 삭제
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reviewService.delete(reviewId, userPrincipal.getEmail());
        return ResponseEntity.ok(ApiUtils.success("리뷰가 삭제되었습니다."));
    }
}
