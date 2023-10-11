package com.ktc.matgpt.feature_review.review.dto;

import com.ktc.matgpt.feature_review.image.Image;
import com.ktc.matgpt.feature_review.review.entity.Review;
import com.ktc.matgpt.feature_review.tag.Tag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewResponse {


    @Getter
    @ToString
    public static class FindByReviewIdDTO {
        private Long reviewId;
        private List<FindByReviewIdDTO.ImageDTO> reviewImages;
        private String content;
        private int rating;
        private int peopleCount;
        private int totalPrice;     // 범위로 받게 된다면 enum PriceRange 타입 이용
        private int costPerPerson;
        private boolean isUpdated = false;
        private String relativeTime;
        public FindByReviewIdDTO(Review review, String relativeTime, List<ImageDTO> reviewImages) {
            this.reviewId = review.getId();
            this.reviewImages = reviewImages;
            this.content = review.getContent();
            this.rating = review.getRating();
            this.peopleCount = review.getPeopleCount();
            this.totalPrice = review.getTotalPrice();
            this.costPerPerson = review.getCostPerPerson();
            this.relativeTime = relativeTime;
            if (review.getCreatedAt() != review.getUpdatedAt()) this.isUpdated = true;
        }

        @Getter
        @ToString
        public static class ImageDTO {
            private String imageUrl;
            private List<ImageDTO.TagDTO> tags;
            public ImageDTO(Image image, List<Tag> tags) {
                this.imageUrl = image.getUrl();
                this.tags = tags.stream()
                        .map(t -> new TagDTO(t))
                        .collect(Collectors.toList());
            }

            @Getter
            @ToString
            public static class TagDTO {
                private String name;
                private int location_x;
                private int location_y;
                private double rating;

                public TagDTO(Tag tag) {
                    this.name = tag.getMenu_name();
                    this.location_x = tag.getLocation_x();
                    this.location_y = tag.getLocation_y();
                    this.rating = tag.getMenu_rating();
                }
            }
        }
    }


    @Getter
    @ToString
    public static class FindAllByStoreIdDTO {
        private Long reviewId;
        private List<String> imageUrls;
        private String content;
        private int rating;
        private LocalDateTime createdAt;
        private boolean isUpdated = false;
        private String relativeTime;

        public FindAllByStoreIdDTO(Review review, String relativeTime, List<String> imageUrls) {
            this.reviewId = review.getId();
            this.imageUrls = imageUrls;
            this.content = review.getContent();
            this.rating = review.getRating();
            this.createdAt = review.getCreatedAt();
            this.relativeTime = relativeTime;

            if (review.getCreatedAt() != review.getUpdatedAt()) this.isUpdated = true;
        }
    }
}
