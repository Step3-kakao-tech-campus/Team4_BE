package com.ktc.matgpt.review.dto;

import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReviewRequest {

    // 초기 리뷰 생성을 위한 간소화된 DTO
    @Getter @Setter
    @ToString
    @NoArgsConstructor
    public static class SimpleCreateDTO {
        private String content;
        private int rating;
        private int peopleCount;
        private int totalPrice;
        private int imageCount; //TODO: enum PriceRange 타입 변환 메서드 만들어야할 것


        public SimpleCreateDTO(String content, int rating, int peopleCount, int totalPrice, int imageCount) {
            this.content = content;
            this.rating = rating;
            this.peopleCount = peopleCount;
            this.totalPrice = totalPrice;
            this.imageCount = imageCount;
        }
    }

    //이미지 업로드 후 리뷰 저장을 위한 DTO
    @Getter
    @ToString
    @NoArgsConstructor
    public static class CreateCompleteDTO {
        private List<ImageDTO> reviewImages;

        public CreateCompleteDTO(List<ImageDTO> reviewImages) {
            this.reviewImages = reviewImages;
        }

        @Getter
        @ToString
        @NoArgsConstructor
        public static class ImageDTO {
            private String imageUrl;
            private List<TagDTO> tags;

            public ImageDTO(String imageUrl, List<TagDTO> tags) {
                this.imageUrl = imageUrl;
                this.tags = tags;
            }

            @Getter
            @ToString
            @NoArgsConstructor
            public static class TagDTO {
                private String name;
                private double locationX;
                private double locationY;
                private int rating;

                public TagDTO(String name, double locationX, double locationY, int rating) {
                    this.name = name;
                    this.locationX = locationX;
                    this.locationY = locationY;
                    this.rating = rating;
                }
            }
        }
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class UpdateDTO {
        private String content;
        public UpdateDTO(String content) {
            this.content = content;
        }
    }


}
