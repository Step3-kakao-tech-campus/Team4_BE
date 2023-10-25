package com.ktc.matgpt.review.dto;

import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReviewRequest {

    @Getter
    @ToString
    public static class CreateDTO {
        private List<ImageDTO> reviewImages;
        private String content;
        private double rating;
        @Min(1)
        private int peopleCount;
        @Min(0)
        private int totalPrice;     // 범위로 받게 된다면 enum PriceRange 타입 이용

        public CreateDTO(List<ImageDTO> reviewImages, String content,
                         double rating, int peopleCount, int totalPrice) {
            this.reviewImages = reviewImages;
            this.content = content;
            this.rating = rating;
            this.peopleCount = peopleCount;
            this.totalPrice = totalPrice;
        }

        @Getter
        @ToString
        @NoArgsConstructor(force = true)
        public static class ImageDTO {
            private MultipartFile image;
            private String imageUrl;
            private List<TagDTO> tags;

            @Getter
            @ToString
            public static class TagDTO {
                private String name;
                private int locationX;
                private int locationY;
                private double rating;
            }
        }
    }


    @Getter
    @ToString
    public static class UpdateDTO {
        private String content;
    }


}
