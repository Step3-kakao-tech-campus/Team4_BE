package com.ktc.matgpt.feature_review.review.dto;

import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReviewRequest {

    @Getter
    @ToString
    public static class CreateDTO {
        @NonNull
        private List<ImageDTO> reviewImages;
        @NonNull
        private String content;
        @NonNull
        private int rating;
        @Min(1)
        private int peopleCount;
        @NonNull
        @Min(0)
        private int totalPrice;     // 범위로 받게 된다면 enum PriceRange 타입 이용

        public CreateDTO(List<ImageDTO> reviewImages, String content,
                         int rating, int peopleCount, int totalPrice) {
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
//            private MultipartFile image;
            private String imageUrl;
            @NonNull
            private List<TagDTO> tags;

            @Getter
            @ToString
            public static class TagDTO {
                @NonNull
                private String name;
                @NonNull
                private int location_x;
                @NonNull
                private int location_y;
                @NonNull
                private double rating;
            }
        }
    }


    @Getter
    @ToString
    public static class UpdateDTO {
        @NonNull
        private String content;
    }


}
