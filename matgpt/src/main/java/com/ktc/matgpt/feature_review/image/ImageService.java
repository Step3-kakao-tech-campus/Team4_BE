package com.ktc.matgpt.feature_review.image;

import com.ktc.matgpt.feature_review.review.entity.Review;
import com.ktc.matgpt.feature_review.s3.S3Service;
import com.ktc.matgpt.feature_review.tag.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return image;
    }

    @Transactional
    public void deleteImagesByReviewId(Long reviewId) {
        List<Image> images = imageJPARepository.findAllByReviewId(reviewId);
        if (images.isEmpty()) {
            log.info("review-" + reviewId + ": 해당 리뷰에 삭제할 이미지가 없습니다.");
            return;
        }
        deleteImagesAndAssociatedTags(images);
    }

    private void deleteImagesAndAssociatedTags(List<Image> images) {
        for (Image image : images) {
            tagService.deleteTagsByImageId(image.getId());
            imageJPARepository.delete(image);
        }
    }

    public List<Image> getImagesByReviewId(Long reviewId){
        return imageJPARepository.findAllByReviewId(reviewId);
    }

    public List<String> getImageUrlsByReviewId(Long reviewId){
        return imageJPARepository.findAllImagesByReviewId(reviewId);
    }
}
