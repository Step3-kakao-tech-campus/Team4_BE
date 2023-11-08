package com.ktc.matgpt.like.likeStore;

import com.ktc.matgpt.store.Store;
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
}
