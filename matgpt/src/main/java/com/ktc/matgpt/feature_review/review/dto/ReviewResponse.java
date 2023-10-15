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
        private Long storeId;
        private Long reviewId;
        private ReviewerDTO reviewer;
        private int averageCostPerPerson;
        private int peopleCount;
        private String createdAt;
        private double rating;
        private int recommendCount;
        private String content;
        private List<FindByReviewIdDTO.ImageDTO> reviewImages;
        private int totalPrice;     // 범위로 받게 된다면 enum PriceRange 타입 이용
        private boolean isUpdated = false;

        public FindByReviewIdDTO(Review review, ReviewerDTO reviewer, List<ImageDTO> reviewImages, String relativeTime) {
            this.storeId = review.getStore().getId();
            this.reviewId = review.getId();
            this.reviewer = reviewer;
            this.reviewImages = reviewImages;
            this.content = review.getContent();
            this.rating = review.getRating();
            this.recommendCount = review.getRecommendCount();
            this.peopleCount = review.getPeopleCount();
            this.totalPrice = review.getTotalPrice();
            this.averageCostPerPerson = review.getCostPerPerson();
            this.createdAt = relativeTime;
            if (review.getCreatedAt() != review.getUpdatedAt()) this.isUpdated = true;
        }

        @Getter
        @ToString
        public static class ReviewerDTO {
            private String profileImage;
            private String userName;
            private String email;

            @Builder
            public ReviewerDTO(String profileImage, String userName, String email) {
                this.profileImage = profileImage;
                this.userName = userName;
                this.email = email;
            }
        }

        @Getter
        @ToString
        public static class ImageDTO {
            private String image;
            private List<ImageDTO.TagDTO> tags;
            public ImageDTO(Image image, List<Tag> tags) {
                this.image = image.getUrl();
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
        private Long userId;
        private double rating;
        private String content;
        private LocalDateTime createdAt;

        private List<String> imageUrls;
        private boolean isUpdated = false;
        private String relativeTime;


        public FindAllByStoreIdDTO(Review review, String relativeTime, List<String> imageUrls) {
            this.reviewId = review.getId();
            this.userId = review.getUserId();
            this.imageUrls = imageUrls;
            this.content = review.getContent();
            this.rating = review.getRating();
            this.createdAt = review.getCreatedAt();
            this.relativeTime = relativeTime;

            if (review.getCreatedAt() != review.getUpdatedAt()) this.isUpdated = true;
        }
    }
}
