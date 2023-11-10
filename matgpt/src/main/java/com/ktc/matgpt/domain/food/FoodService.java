package com.ktc.matgpt.domain.food;

import com.ktc.matgpt.domain.food.dto.FoodDTO;
import com.ktc.matgpt.domain.review.dto.ReviewRequest;
import com.ktc.matgpt.domain.store.Store;
import com.ktc.matgpt.domain.store.StoreService;
import com.ktc.matgpt.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FoodService {
    private final FoodJPARepository foodJPARepository;
    private final StoreService storeService;

    @Transactional
    public Food saveOrUpdateFoodByTagName(ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tagDTO, Long storeId) {
        return foodJPARepository.findByFoodName(tagDTO.getName())
                .map(food -> {
                    food.addReview(tagDTO.getRating());
                    return food;
                })
                .orElseGet(() -> addFoodToStore(storeId,new FoodDTO.CreateDTO(tagDTO.getName(), tagDTO.getRating())));
    }

    @Transactional
    public void removeRatingByTagName(Tag tag) {
        foodJPARepository.findByFoodName(tag.getTagName())
                .ifPresent(food -> food.removeReview(tag.getMenuRating()));
    }


    @Transactional(readOnly = true)
    public List<Food> getFoodsByStore(Long storeId) {
        return foodJPARepository.findByStoreId(storeId);
    }

    @Transactional(readOnly = true)
    public List<Food> getRecentlySearchedFoodsByStore(Long storeId) {
        return foodJPARepository.findTop10ByStoreIdOrderByCreatedAtDesc(storeId);
    }

    @Transactional
    public Food addFoodToStore(Long storeId,FoodDTO.CreateDTO dto) {
        Store store = storeService.findById(storeId);
        return foodJPARepository.save(Food.create(dto.getFoodName(), dto.getFirstRating(), store));
    }
}