package com.ktc.matgpt.feature_review.image;

import com.ktc.matgpt.feature_review.errors.exception.Exception500;
import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import com.ktc.matgpt.feature_review.review.entity.Review;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageJPARepository imageJPARepository;

    public Image save(ReviewRequest.CreateDTO.ImageDTO imageDTO, Review review/*, String imageUrl*/) {
        Image image = Image.builder()
                .review(review)
//                .url(imageUrl)
                .url(imageDTO.getImageUrl())
                .build();

        try {
            imageJPARepository.save(image);
        } catch (Exception e) {
            throw new Exception500("이미지 저장 실패");
        }

        return image;
    }

}
