package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LikeReviewResponseDTO {

    @Getter @Setter
    public static class FindLikeReviewsDTO {
        List<LikeReviewDTO> reviews;
        public FindLikeReviewsDTO(List<LikeReview> likeReviews) {
            this.reviews = likeReviews.stream().map(
                    likeReview -> {
                        Review review = likeReview.getReview();
                        User reviewer = likeReview.getUser();
                        return new LikeReviewDTO(review, reviewer);
                    }).collect(Collectors.toList());
        }

        @Getter @Setter
        public static class LikeReviewDTO {
            private Long id;
            private double rating;
            private String content;
            private LocalDateTime createdAt;
            private String storeImage;
            private String storeName;
            private int peopleCount;
            private String reviewerName;
            private String reviewerImage;

            public LikeReviewDTO(Review review, User reviewer){
                this.id = review.getId();
                this.rating = review.getRating();
                this.content = review.getContent();
                this.createdAt = review.getCreatedAt();
                this.storeImage = review.getStore().getStoreImageUrl();
                this.storeName = review.getStore().getName();
                this.peopleCount = review.getPeopleCount();
                this.reviewerName = reviewer.getName();
                this.reviewerImage = reviewer.getProfileImageUrl();
            }
        }
    }
}

