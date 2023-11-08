package com.ktc.matgpt.food;

import com.ktc.matgpt.food.dto.FoodDTO;
import com.ktc.matgpt.review.dto.ReviewRequest;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreService;
import com.ktc.matgpt.tag.Tag;
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

    public void removeRatingByTagName(Tag tag) {
        foodJPARepository.findByFoodName(tag.getTagName())
                .ifPresent(food -> food.removeReview(tag.getMenuRating()));
    }



    public List<Food> getFoodsByStore(Long storeId) {
        return foodJPARepository.findByStoreId(storeId);
    }

    public List<Food> getRecentlySearchedFoodsByStore(Long storeId) {
        return foodJPARepository.findTop10ByStoreIdOrderByCreatedAtDesc(storeId);
    }


    public Food addFoodToStore(Long storeId,FoodDTO.CreateDTO dto) {
        Store store = storeService.findById(storeId);
        return foodJPARepository.save(Food.create(dto.getFoodName(), dto.getFirstRating(), store));
    }
}