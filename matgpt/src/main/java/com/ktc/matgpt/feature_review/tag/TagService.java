package com.ktc.matgpt.feature_review.tag;

import com.ktc.matgpt.feature_review.food.Food;
import com.ktc.matgpt.feature_review.image.Image;
import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
        tagJPARepository.save(tag);

        return tag;
    }

    @Transactional
    public void deleteAll(Long imageId) {
        List<Tag> tags = tagJPARepository.findAllByImageId(imageId);
        if (tags.isEmpty()) {
            log.info("image-" + imageId + ": 해당 이미지에 삭제할 태그가 없습니다.");
            return;
        }

        for (Tag tag : tags) {
            Food food = tag.getFood();
            food.updateReviewDecrease(tag.getMenu_rating());
            tagJPARepository.delete(tag);
        }
    }
}
