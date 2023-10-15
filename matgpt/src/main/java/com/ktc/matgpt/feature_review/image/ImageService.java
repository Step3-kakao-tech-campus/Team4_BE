package com.ktc.matgpt.feature_review.image;

import com.ktc.matgpt.exception.Exception400;
import com.ktc.matgpt.exception.Exception500;
import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import com.ktc.matgpt.feature_review.review.entity.Review;
import com.ktc.matgpt.feature_review.s3.S3Service;
import com.ktc.matgpt.feature_review.tag.TagJPARepository;
import com.ktc.matgpt.feature_review.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        if (images.isEmpty()) throw new Exception500("reviewId" + reviewId + "해당 리뷰에 등록된 이미지가 없습니다.");

        for (Image image : images) {
            tagService.deleteAll(image.getId());
            imageJPARepository.delete(image);
        }
    }
}
