package com.ktc.matgpt.like.likeStore;

import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class LikeStoreResponseDTO {

    @Getter
    @Setter
    public static class FindAllLikeStoresDTO{

        private Long userId;
        private List<StoreDTO> storeList;

        public FindAllLikeStoresDTO(User user, List<Store> storeList){
            this.userId = user.getId();
            this.storeList = storeList.stream().map(StoreDTO::new).collect(Collectors.toList());


        }

        @Getter @Setter
        public class StoreDTO{
            private Long storeId;
            private String storeName;
            private double ratingAvg;
            private int numsOfReview;

            public StoreDTO(Store store){
                this.storeId = store.getId();
                this.storeName = store.getName();
                this.ratingAvg = store.getRatingAvg();
                this.numsOfReview = store.getNumsOfReview();
            }
        }
    }
}
