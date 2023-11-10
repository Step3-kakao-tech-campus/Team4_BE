package com.ktc.matgpt.domain.image;

import com.ktc.matgpt.domain.aws.FileValidator;
import com.ktc.matgpt.domain.review.entity.Review;
import com.ktc.matgpt.domain.aws.S3Service;
import com.ktc.matgpt.domain.tag.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageJPARepository imageJPARepository;
    private final TagService tagService;
    private final S3Service s3Service;

    @Transactional
    public Image saveImageForReview(Review review, String presignedUrl) {
        log.info("presignedUrl : {}", presignedUrl);
        String s3Url = s3Service.getS3Url(presignedUrl);

        Image image = Image.create(review, s3Url);
        imageJPARepository.save(image);
        log.info("image-{}: 이미지가 저장되었습니다.", image.getId());
        return image;
    }

    @Transactional
    public void deleteImagesByReviewId(Long reviewId) {
        List<Image> images = imageJPARepository.findAllByReviewId(reviewId);
        if (images.isEmpty()) {
            log.info("review-{}: 해당 리뷰에 삭제할 이미지가 없습니다.", reviewId);
            return;
        }
        deleteImagesAndAssociatedTags(images);
    }

    private void deleteImagesAndAssociatedTags(List<Image> images) {
        for (Image image : images) {
            tagService.deleteTagsByImageId(image.getId());
            s3Service.deleteImage(image.getUrl());
            imageJPARepository.delete(image);
            log.info("image-{}: 이미지를 삭제했습니다.", image.getId());
        }
    }

    @Transactional(readOnly = true)
    public List<Image> getImagesByReviewId(Long reviewId){
        return imageJPARepository.findAllByReviewId(reviewId);
    }

    @Transactional(readOnly = true)
    public List<String> getImageUrlsByReviewId(Long reviewId){
        return imageJPARepository.findAllImagesByReviewId(reviewId);
    }

    @Transactional(readOnly = true)
    public String getFirstImageByReviewId(Long reviewId){
        List<String> image = imageJPARepository.findFirstImageByReviewId(reviewId, PageRequest.ofSize(1));
        return (image.isEmpty()) ? null : image.get(0);
    }

    @Deprecated
    public void validateImageFile(MultipartFile image) throws FileValidator.FileValidationException {
        FileValidator.validateFile(image);
    }
}
