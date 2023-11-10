package com.ktc.matgpt.domain.tag;

import com.ktc.matgpt.domain.food.Food;
import com.ktc.matgpt.domain.food.FoodService;
import com.ktc.matgpt.domain.image.Image;
import com.ktc.matgpt.domain.review.dto.ReviewRequest;
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
    private final FoodService foodService;


    public Tag saveTag(Image image, Food food, ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tagDTO) {
        Tag tag = Tag.create(image,food, tagDTO.getName(), tagDTO.getRating(), tagDTO.getLocationX(),tagDTO.getLocationY());
        tagJPARepository.save(tag);
        log.info("tag-{}: 태그가 저장되었습니다.", tag.getId());
        return tag;
    }


    @Transactional
    public void deleteTagsByImageId(Long imageId) {
        List<Tag> tags = getTagsByImageId(imageId);

        if (tags.isEmpty()) {
            log.info("image-{}: 해당 이미지에 삭제할 태그가 없습니다.", imageId);
            return;
        }

        updateFoodBeforeDelete(tags); // 필요한 업데이트를 먼저 처리
        bulkDeleteTags(tags); // 태그들을 Bulk Delete를 통해 삭제
    }

    public List<Tag> getTagsByImageId(Long imageId) {
        return tagJPARepository.findAllByImageId(imageId);
    }


    private void updateFoodBeforeDelete(List<Tag> tags) {
        tags.forEach(foodService::removeRatingByTagName);
    }

    private void bulkDeleteTags(List<Tag> tags) {
        tagJPARepository.deleteAllInBatch(tags); // Spring Data JPA의 Bulk Delete를 수행
        tags.forEach(tag -> log.info("tag-{}: 태그를 삭제했습니다.", tag.getId()));
    }
}
