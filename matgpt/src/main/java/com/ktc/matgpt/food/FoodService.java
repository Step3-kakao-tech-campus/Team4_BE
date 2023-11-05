package com.ktc.matgpt.food;

import com.ktc.matgpt.food.dto.FoodDTO;
import com.ktc.matgpt.review.dto.ReviewRequest;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FoodService {
    private final FoodJPARepository foodJPARepository;
    private final StoreService storeService;

    public Food saveOrUpdateFoodByTag(ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tagDTO, Long storeId) {
        return foodJPARepository.findByFoodName(tagDTO.getName())
                .map(food -> {
                    updateFoodRating(food, tagDTO.getRating());
                    return food;
                })
                .orElseGet(() -> addFoodToStore(storeId,new FoodDTO.CreateDTO(tagDTO.getName(),"")));
    }

    private void updateFoodRating(Food food, double newRating) {
        food.updateReview(food.getTotalRating(), newRating);
    }

    public void removeRatingByTagName(ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tagDTO) {
        foodJPARepository.findByFoodName(tagDTO.getName())
                .ifPresent(food -> food.removeReview(tagDTO.getRating()));
    }



    public List<Food> getFoodsByStore(Long storeId) {
        return foodJPARepository.findByStoreId(storeId);
    }

    public List<Food> getRecentlySearchedFoodsByStore(Long storeId) {
        return foodJPARepository.findTop10ByStoreIdOrderByCreatedAtDesc(storeId);
    }


    public Food addFoodToStore(Long storeId,FoodDTO.CreateDTO dto) {
        Store store = storeService.findById(storeId);
        return foodJPARepository.save(Food.create(dto.getFoodName(),dto.getFoodDescription(),store));
    }
}