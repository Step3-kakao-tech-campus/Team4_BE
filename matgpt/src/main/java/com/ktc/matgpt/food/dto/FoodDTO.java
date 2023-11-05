package com.ktc.matgpt.food.dto;

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

