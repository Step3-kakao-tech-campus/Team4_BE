package com.ktc.matgpt.food.dto;

import lombok.Getter;
import lombok.ToString;

public class FoodDTO {

    @Getter
    @ToString
    public static class CreateDTO {
        private String foodName;
        private String foodDescription;
        private int firstRating;

        public CreateDTO(String foodName, String foodDescrption, int firstRating) {
            this.foodName = foodName;
            this.foodDescription = foodDescrption;
            this.firstRating = firstRating;
        }
    }


}

