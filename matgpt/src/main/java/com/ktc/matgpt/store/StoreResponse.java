package com.ktc.matgpt.store;
import com.ktc.matgpt.store.entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class StoreResponse {
    private Long storeId;
    private String storeName;
    @Builder
    public StoreResponse(Long storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }

    @Getter
    @Setter
    public static class FindAllStoreDTO {
        private Long storeId;
        private String storeName;
        private Category category;
        private double ratingAvg;
        private int numsOfReview;

        public FindAllStoreDTO(Store store){
            this.storeId = store.getId();
            this.storeName = store.getStoreName();
            this.category = store.getCategory();
            this.ratingAvg = store.getRatingAvg();
            this.numsOfReview = store.getNumsOfReview();
        }



    }

    public static class FindByIdStoreDTO{

    }


}