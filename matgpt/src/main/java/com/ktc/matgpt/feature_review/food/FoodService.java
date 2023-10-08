package com.ktc.matgpt.feature_review.food;

import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import com.ktc.matgpt.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FoodService {
    private final FoodJPARepository foodJPARepository;
    public List<Food> createAll(ReviewRequest.CreateDTO requestDTO) {
        List<Food> foods = new ArrayList<>();
        List<ReviewRequest.CreateDTO.ImageDTO> imageDTOs = requestDTO.getReviewImages();

        for (ReviewRequest.CreateDTO.ImageDTO imageDTO : imageDTOs) {
            List<ReviewRequest.CreateDTO.ImageDTO.TagDTO> tagDTOs = imageDTO.getTags();

            for (ReviewRequest.CreateDTO.ImageDTO.TagDTO tagDTO : tagDTOs) {
                foods.add(save(tagDTO));
            }
        }
        return foods;
    }

    public Food save(ReviewRequest.CreateDTO.ImageDTO.TagDTO tagDTO) {
        Food food = Food.builder()
                        .foodName(tagDTO.getName())
                        .averageRating(tagDTO.getRating())
                        .build();

        try {
            foodJPARepository.save(food);
        } catch (Exception e) {
            throw new Exception500("음식메뉴 저장 실패");
        }

        return food;
    }
}