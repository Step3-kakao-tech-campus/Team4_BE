package com.ktc.matgpt.domain.like.likeStore;

import com.ktc.matgpt.domain.store.Store;
import lombok.Getter;
import lombok.Setter;

public class LikeStoreResponseDTO {

    @Getter
    @Setter
    public static class FindAllLikeStoresDTO{

        private Long storeId;
        private String storeName;
        private double ratingAvg;
        private int numsOfReview;

        public FindAllLikeStoresDTO(Store store){
            this.storeId = store.getId();
            this.storeName = store.getName();
            this.ratingAvg = store.getAvgRating();
            this.numsOfReview = store.getNumsOfReview();
        }

    }

    @Getter
    @Setter
    public static class HasLikedDTO {
        boolean hasLiked;
        public HasLikedDTO(boolean hasLiked) {
            this.hasLiked = hasLiked;
        }
    }
}
