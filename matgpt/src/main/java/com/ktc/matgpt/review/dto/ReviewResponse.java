package com.ktc.matgpt.review.dto;

import com.ktc.matgpt.image.Image;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.tag.Tag;
import lombok.*;
import org.springframework.data.domain.Page;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewResponse {

    @Getter
    @ToString
    public static class UploadS3DTO {
        private String reviewUuid;
        private List<PresignedUrlDTO> presignedUrls;

        public UploadS3DTO(String reviewUuid, List<PresignedUrlDTO> presignedUrls) {
            this.reviewUuid = reviewUuid;
            this.presignedUrls = presignedUrls;
        }

        @Getter
        @ToString
        public static class PresignedUrlDTO {
            private String objectKey;
            private URL presignedUrl;

            @Builder
            public PresignedUrlDTO(String objectKey, URL presignedUrl) {
                this.objectKey = objectKey;
                this.presignedUrl = presignedUrl;
            }
        }
    }


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
                    this.name = tag.getFood().getFoodName();
                    this.location_x = tag.getLocationX();
                    this.location_y = tag.getLocationY();
                    this.rating = tag.getFood().getAverageRating();
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

    @Getter
    @ToString
    public static class FindPageByUserIdDTO {
        List<FindByUserIdDTO> reviews;
        private int totalPageCount;
        private int currentReviewCount;
        private boolean hasNextPage;
        public FindPageByUserIdDTO(List<FindByUserIdDTO> reviewDTOs, Page<Review> reviewPage) {
            this.reviews = reviewDTOs;
            this.totalPageCount = reviewPage.getTotalPages();
            this.currentReviewCount = reviewPage.getNumberOfElements();
            this.hasNextPage = reviewPage.hasNext();
        }

        @Getter
        @ToString
        public static class FindByUserIdDTO {
            private Long reviewId;
            private double rating;
            private String content;
            private LocalDateTime createdAt;
            private String storeImage;
            private String storeName;
            private List<String> imageUrls;
            private String relativeTime;
            private boolean isUpdated = false;


            public FindByUserIdDTO(Review review, String relativeTime, List<String> imageUrls) {
                this.reviewId = review.getId();
                this.rating = review.getRating();
                this.content = review.getContent();
                this.createdAt = review.getCreatedAt();
                this.storeImage = review.getStore().getStoreImageUrl();
                this.storeName = review.getStore().getName();
                this.imageUrls = imageUrls;
                this.relativeTime = relativeTime;
                if (review.getCreatedAt() != review.getUpdatedAt()) this.isUpdated = true;
            }
        }
    }
}
