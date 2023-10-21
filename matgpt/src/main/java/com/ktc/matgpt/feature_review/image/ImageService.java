package com.ktc.matgpt.feature_review.image;

import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
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

    public Image save(ReviewRequest.CreateDTO.ImageDTO imageDTO, Review review/*, String imageUrl*/) {
        Image image = Image.builder()
                .review(review)
//                .url(imageUrl)
                .url(imageDTO.getImageUrl())
                .build();
        imageJPARepository.save(image);

        return image;
    }

    @Transactional
    public void deleteAll(Long reviewId) {
        List<Image> images = imageJPARepository.findAllByReviewId(reviewId);
        if (images.isEmpty()) {
            log.info("review-" + reviewId + ": 해당 리뷰에 삭제할 이미지가 없습니다.");
            return;
        }

        for (Image image : images) {
            tagService.deleteAll(image.getId());
            imageJPARepository.delete(image);
        }
    }
}
