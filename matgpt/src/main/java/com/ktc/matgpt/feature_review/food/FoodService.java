package com.ktc.matgpt.feature_review.food;

import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import com.ktc.matgpt.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FoodService {
    private final FoodJPARepository foodJPARepository;
    public List<Food> createAll(ReviewRequest.CreateDTO requestDTO) {
        List<Food> foods = new ArrayList<>();
        List<ReviewRequest.CreateDTO.ImageDTO> imageDTOs = requestDTO.getReviewImages();

        imageDTOs.stream().map(
                imageDTO -> imageDTO.getTags().stream().map(
                        tagDTO -> foods.add(save(tagDTO))
                )
        );

        return foods;
    }

    public Food save(ReviewRequest.CreateDTO.ImageDTO.TagDTO tagDTO) {
        Optional<Food> foodPS = foodJPARepository.findByFoodName(tagDTO.getName());
        if (!foodPS.isEmpty()) {
            Food food = foodPS.get();
            food.updatePlus(tagDTO.getRating());
            return food;
        }
        Food food = Food.builder()
                .foodName(tagDTO.getName())
                .reviewCount(1)
                .averageRating(tagDTO.getRating())
                .build();
        foodJPARepository.save(food);

        return food;
    }
}