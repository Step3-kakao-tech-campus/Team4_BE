package com.ktc.matgpt.domain.food.dto;

import lombok.Getter;
import lombok.ToString;

public class FoodDTO {

    @Getter
    @ToString
    public static class CreateDTO {
        private String foodName;
        private int firstRating;

        public CreateDTO(String foodName, int firstRating) {
            this.foodName = foodName;
            this.firstRating = firstRating;
        }
    }


}

