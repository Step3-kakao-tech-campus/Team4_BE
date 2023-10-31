package com.ktc.matgpt.review;

import com.ktc.matgpt.aws.FileValidator;
import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;
import com.ktc.matgpt.image.ImageService;
import com.ktc.matgpt.review.dto.ReviewRequest;
import com.ktc.matgpt.review.dto.ReviewResponse;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping(value = "/stores", produces = {"application/json; charset=UTF-8"})
@RequiredArgsConstructor
@RestController
public class ReviewRestController {
    private final ReviewService reviewService;
    private final ImageService imageService;

    // 첫 번째 단계: 리뷰 임시 저장 및 Presigned URL 반환
    @PostMapping(value = "/{storeId}/reviews/temp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createTemporaryReview(@PathVariable Long storeId,
                                                                        @RequestPart("data") ReviewRequest.SimpleCreateDTO requestDTO,
                                                                        @RequestPart("images") List<MultipartFile> images,
                                                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            //파일 검증
            for (MultipartFile image : images) {
                imageService.validateImageFile(image);
            }
            String reviewUuid = reviewService.createTemporaryReview(userPrincipal.getId(), storeId, requestDTO);
            List<ReviewResponse.UploadS3DTO.PresignedUrlDTO> presignedUrls = reviewService.createPresignedUrls(reviewUuid, images.size());
            return ResponseEntity.ok(ApiUtils.success(new ReviewResponse.UploadS3DTO(reviewUuid, presignedUrls)));
        } catch (FileValidator.FileValidationException e) {
            throw new CustomException(ErrorCode.S3_FILE_VALIDATION_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REVIEW_PROCESS_ERROR);
        }
    }


    // 두 번째 단계: 이미지와 태그 정보를 포함하여 리뷰 완료
    @PostMapping("/{storeId}/complete/{reviewId}")
    public ResponseEntity<?> completeReview(@PathVariable Long storeId,Long reviewId,
                                            @RequestBody ReviewRequest.CreateCompleteDTO requestDTO) {
        try {
            reviewService.completeReviewUpload(storeId, reviewId, requestDTO);
            return ResponseEntity.ok(ApiUtils.success("리뷰가 성공적으로 완료되었습니다."));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REVIEW_PROCESS_ERROR);
        }
    }

    // 음식점 리뷰 목록 조회
    @GetMapping("/{storeId}/reviews")
    public ResponseEntity<?> findAllByStoreId(@PathVariable Long storeId,
                                              @RequestParam(defaultValue = "latest") String sortBy,
                                              @RequestParam(defaultValue = "6") Long cursorId,
                                              @RequestParam(defaultValue = "5.0") double cursorRating) {
        List<ReviewResponse.FindAllByStoreIdDTO> responseDTOs = reviewService.findAllByStoreId(storeId, sortBy, cursorId, cursorRating);
        return ResponseEntity.ok(ApiUtils.success(responseDTOs));
    }


    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> update(@PathVariable Long reviewId,
                                    @RequestBody @Valid ReviewRequest.UpdateDTO requestDTO,
                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reviewService.update(reviewId, userPrincipal.getId(), requestDTO);
        String msg = "review-" + reviewId + " updated";
        return ResponseEntity.ok(ApiUtils.success(msg));
    }

    // 개별 리뷰 상세조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> findById(@PathVariable Long reviewId) {
        ReviewResponse.FindByReviewIdDTO responseDTO = reviewService.findDetailByReviewId(reviewId);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }

    // TODO: s3 삭제 구현
    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reviewService.delete(reviewId, userPrincipal.getId());
        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
