package com.ktc.matgpt.image;

import com.ktc.matgpt.aws.FileValidator;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.aws.S3Service;
import com.ktc.matgpt.tag.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageJPARepository imageJPARepository;
    private final TagService tagService;
    private final S3Service s3Service;

    public Image saveImageForReview(Review review, String imageUrl) {
        Image image = Image.create(review, imageUrl);
        imageJPARepository.save(image);
        log.info("image-%d: 이미지가 저장되었습니다.", image.getId());
        return image;
    }

    @Transactional
    public void deleteImagesByReviewId(Long reviewId) {
        List<Image> images = imageJPARepository.findAllByReviewId(reviewId);
        if (images.isEmpty()) {
            log.info("review-%d: 해당 리뷰에 삭제할 이미지가 없습니다.", reviewId);
            return;
        }
        deleteImagesAndAssociatedTags(images);
    }

    private void deleteImagesAndAssociatedTags(List<Image> images) {
        for (Image image : images) {
            tagService.deleteTagsByImageId(image.getId());
            imageJPARepository.delete(image);
            log.info("image-%d: 이미지를 삭제했습니다.", image.getId());
        }
    }

    public List<Image> getImagesByReviewId(Long reviewId){
        return imageJPARepository.findAllByReviewId(reviewId);
    }

    public List<String> getImageUrlsByReviewId(Long reviewId){
        return imageJPARepository.findAllImagesByReviewId(reviewId);
    }

    public void validateImageFile(MultipartFile image) throws FileValidator.FileValidationException {
        FileValidator.validateFile(image);
    }



}
