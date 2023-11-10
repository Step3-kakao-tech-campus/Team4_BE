package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class LikeReviewResponse {

    @Getter @Setter
    public static class LikeReviewDTO {
        private Long reviewId;
        private int rating;
        private String content;
        private LocalDateTime createdAt;
        private String relativeTime;
        private String imageUrl;
        private Long storeId;
        private String storeImage;
        private String storeName;
        private int peopleCount;
        private String reviewerName;
        private String reviewerImage;

        public LikeReviewDTO(Review review, String relativeTime, String imageUrl) {
            this.reviewId = review.getId();
            this.rating = review.getRating();
            this.content = review.getContent();
            this.createdAt = review.getCreatedAt();
            this.relativeTime = relativeTime;
            this.imageUrl = imageUrl;
            this.storeId = review.getStore().getId();
            this.storeImage = review.getStore().getStoreImageUrl();
            this.storeName = review.getStore().getName();
            this.peopleCount = review.getPeopleCount();
            this.reviewerName = review.getUser().getName();
            this.reviewerImage = review.getUser().getProfileImageUrl();
        }
    }

}

