package com.ktc.matgpt.feature_review.tag;

import com.ktc.matgpt.exception.Exception500;
import com.ktc.matgpt.feature_review.food.Food;
import com.ktc.matgpt.feature_review.image.Image;
import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TagService {
    private final TagJPARepository tagJPARepository;

    public Tag save(Image image, Food food, ReviewRequest.CreateDTO.ImageDTO.TagDTO tagDTO) {
        Tag tag = Tag.builder()
                .image(image)
                .food(food)
                .menu_name(tagDTO.getName())
                .menu_rating(tagDTO.getRating())
                .location_x(tagDTO.getLocation_x())
                .location_y(tagDTO.getLocation_y())
                .build();
        try {
            tagJPARepository.save(tag);
        } catch (Exception e) {
            throw new Exception500("태그 정보 저장 실패");
        }
        return tag;
    }

}
