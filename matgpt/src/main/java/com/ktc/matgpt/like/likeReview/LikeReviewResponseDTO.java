package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.like.likeStore.LikeStoreResponseDTO;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class LikeReviewResponseDTO {
    @Getter
    @Setter
    public static class FindAllLikeReviewsDTO{

        private List<LikeReviewResponseDTO.FindAllLikeReviewsDTO.ReviewDTO> reviewList;

        public FindAllLikeReviewsDTO(User user, List<Review> reviewList){
            this.reviewList = reviewList.stream()
                    .map(review -> new ReviewDTO(review, user)) // Review와 User 객체 모두 전달
                    .collect(Collectors.toList());
        }

        @Getter @Setter
        public class ReviewDTO{
            private Long reviewId;
            private String reviewContent;
            private double rating;
            private int peopleCount;
            private int numOfLikes;
            private StoreDTO store;
            private ReviewerDTO reviewer;

            public ReviewDTO(Review review,User user){
                this.reviewId = review.getId();
                this.reviewContent = review.getContent();
                this.rating = review.getRating();
                this.peopleCount = review.getPeopleCount();
                this.numOfLikes = review.getRecommendCount();
                this.store = new StoreDTO(review.getStore());
                this.reviewer = new ReviewerDTO(user); // ReviewerDTO 초기화
            }

            @Getter @Setter
            public class StoreDTO{
                private Long storeId;
                private String storeName;
                private String storeImage;

                public StoreDTO(Store store){
                    this.storeId = store.getId();
                    this.storeName = store.getName();
                    this.storeImage = store.getStoreImageUrl();
                }
            }

            @Getter @Setter
            public class ReviewerDTO{
                private Long userId;
                private String userName;
                private String userProfileImage;

                public ReviewerDTO(User user){
                    this.userId = user.getId();
                    this.userName = user.getName();
                    this.userProfileImage = user.getProfileImageUrl();
                }
            }
        }
    }
}
