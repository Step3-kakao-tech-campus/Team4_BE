package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public class LikeReviewResponse {

    @Getter @Setter
    public static class FindLikeReviewPageDTO {
        PageInfoDTO paging;
        List<ReviewDTO> reviews;
        public FindLikeReviewPageDTO(Page<LikeReview> likeReviews, List<ReviewDTO> reviews) {
            this.paging = new PageInfoDTO(likeReviews);
            this.reviews = reviews;
        }

        @Getter
        @ToString
        public class PageInfoDTO {
            private boolean hasNext;
            private int countOfReviews;
            private Long nextCursorId;
            public PageInfoDTO(Page<LikeReview> page) {
                int count = page.getNumberOfElements();
                LikeReview lastReview = page.getContent().get(count-1);

                this.hasNext = page.hasNext();
                this.countOfReviews = count;
                this.nextCursorId = lastReview.getId();
            }
        }

        @Getter @Setter
        public static class ReviewDTO {
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

            public ReviewDTO(Review review, User reviewer, String relativeTime){
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
}

