package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class LikeReviewResponse {

    @Getter @Setter
    public static class FindLikeReviewPageDTO {
        private Long id;
        private int rating;
        private String content;
        private LocalDateTime createdAt;
        private String relativeTime;
        private String storeImage;
        private String storeName;
        private int peopleCount;
        private String reviewerName;
        private String reviewerImage;

        public FindLikeReviewPageDTO(Review review, User reviewer, String relativeTime) {
            this.id = review.getId();
            this.rating = review.getRating();
            this.content = review.getContent();
            this.createdAt = review.getCreatedAt();
            this.relativeTime = relativeTime;
            this.storeImage = review.getStore().getStoreImageUrl();
            this.storeName = review.getStore().getName();
            this.peopleCount = review.getPeopleCount();
            this.reviewerName = reviewer.getName();
            this.reviewerImage = reviewer.getProfileImageUrl();
        }
    }

}

