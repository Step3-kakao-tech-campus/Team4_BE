package com.ktc.matgpt.feature_review.food;

import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FoodService {
    private final FoodJPARepository foodJPARepository;
    public Food save(ReviewRequest.CreateDTO.ImageDTO.TagDTO tagDTO) {
        return foodJPARepository.findByFoodName(tagDTO.getName())
                .map(food -> {
                    food.updateReviewIncrease(tagDTO.getRating());
                    return food;
                }
        ).orElse(create(tagDTO));
    }

    public Food create(ReviewRequest.CreateDTO.ImageDTO.TagDTO tagDTO) {
        Food newFood = Food.builder()
                .foodName(tagDTO.getName())
                .reviewCount(1)
                .averageRating(tagDTO.getRating())
                .build();
        return foodJPARepository.save(newFood);
    }

}