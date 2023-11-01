package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.store.Store;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

// TODO: api 형식에 맞게 수정 필요
public class LikeReviewResponseDTO {
    List<FindLikeReviewDTO> reviews;
    @Getter @Setter
    public static class FindLikeReviewDTO {
        private Long id;
        private double rating;
        private String content;
        private LocalDateTime createdAt;
        private String storeImage;
        private String storeName;
        private int peopleCount;
        private String reviewerName;
        private String reviewerImage;

        public FindLikeReviewDTO(Review review, String reviewerName, String reviewerImage){
            this.id = review.getId();
            this.rating = review.getRating();
            this.content = review.getContent();
            this.createdAt = review.getCreatedAt();
            this.storeImage = review.getStore().getStoreImg();
            this.storeName = review.getStore().getName();
            this.peopleCount = review.getPeopleCount();
            this.reviewerName = reviewerName;
            this.reviewerImage = reviewerImage;
        }
    }
}

