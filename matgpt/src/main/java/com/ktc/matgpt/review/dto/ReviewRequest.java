package com.ktc.matgpt.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        private double rating;
        private int peopleCount;
        private int totalPrice;

        @Builder
        public SimpleCreateDTO(String content, double rating, int peopleCount, int totalPrice) {
            this.content = content;
            this.rating = rating;
            this.peopleCount = peopleCount;
            this.totalPrice = totalPrice;
        }
    }

    //이미지 업로드 후 리뷰 저장을 위한 DTO
    @Getter
    @ToString
    public static class CreateCompleteDTO {
        private List<ImageDTO> reviewImages;
        private String content;
        private double rating;
        @Min(1)
        private int peopleCount;
        @Min(0)
        private int totalPrice;     //TODO: enum PriceRange 타입 변환 메서드 만들어야할 것

        public CreateCompleteDTO(List<ImageDTO> reviewImages, String content,
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
            private String imageUrl;
            private List<TagDTO> tags;

            public ImageDTO(String imageUrl, List<TagDTO> tags) {
                this.imageUrl = imageUrl;
                this.tags = tags;
            }

            @Getter
            @ToString
            public static class TagDTO {
                private String name;
                private int locationX;
                private int locationY;
                private double rating;

                public TagDTO(String name, int locationX, int locationY, double rating) {
                    this.name = name;
                    this.locationX = locationX;
                    this.locationY = locationY;
                    this.rating = rating;
                }
            }
        }
    }

    @Getter
    @ToString
    public static class CreateDTO {
        private List<ImageDTO> reviewImages;
        private String content;
        private double rating;
        @Min(1)
        private int peopleCount;
        @Min(0)
        private int totalPrice;

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
