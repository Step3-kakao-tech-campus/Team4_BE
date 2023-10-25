package com.ktc.matgpt.food.dto;

import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class FoodDTO {

    @Getter
    @ToString
    public static class CreateDTO {
        private String foodName;
        private String foodDescription;

        public CreateDTO(String foodName, String foodDescrption) {
            this.foodName = foodName;
            this.foodDescription = foodDescrption;
        }
    }


}

