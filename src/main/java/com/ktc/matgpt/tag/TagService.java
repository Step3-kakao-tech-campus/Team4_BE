package com.ktc.matgpt.tag;

import com.ktc.matgpt.food.Food;
import com.ktc.matgpt.image.Image;
import com.ktc.matgpt.review.dto.ReviewRequest;
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


    public Tag saveTag(Image image, Food food, ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tagDTO) {
        Tag tag = Tag.create(image,food, tagDTO.getRating(), tagDTO.getLocationX(),tagDTO.getLocationY());
        tagJPARepository.save(tag);
        log.info("tag-%d: 태그가 저장되었습니다.", tag.getId());
        return tag;
    }


    @Transactional
    public void deleteTagsByImageId(Long imageId) {
        List<Tag> tags = getTagsByImageId(imageId);

        if (tags.isEmpty()) {
            log.info("image-%d: 해당 이미지에 삭제할 태그가 없습니다.", imageId);
            return;
        }

        deleteTagsAndRelatedReviews(tags);
    }

    public List<Tag> getTagsByImageId(Long imageId) {
        return tagJPARepository.findAllByImageId(imageId);
    }


    private void deleteTagsAndRelatedReviews(List<Tag> tags) {
        for (Tag tag : tags) {
            removeReviewFromFood(tag);
            tagJPARepository.delete(tag);
            log.info("tag-%d: 태그를 삭제했습니다.", tag.getId());
        }
    }

    private void removeReviewFromFood(Tag tag) {
        Food food = tag.getFood();
        food.removeReview(tag.getMenuRating());
    }


}
