package com.ktc.matgpt.store;
import com.ktc.matgpt.store.entity.Category;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class StoreResponse {

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

    @Getter
    @Setter
    public static class FindByIdStoreDTO{

        private Long storeId;
        private String storeName;
        private Category category;
        private String detailedCategory;
        private String number;
        private String address;
        private String openingTime;
        private String closingTime;
        private String breakTime;
        private double avgCostPerPerson;
        private int avgVisitCount;
        private String storeImg;
        private int numsOfReview;
        private double ratingAvg;

        public FindByIdStoreDTO(Store store){
            this.storeId = store.getId();
            this.storeName = store.getStoreName();
            this.category = store.getCategory();
            this.detailedCategory = store.getDetailedCategory();
            this.number = store.getNumber();
            this.address = store.getAddress();
            this.openingTime = store.getOpeningTime();
            this.closingTime = store.getClosingTime();
            this.breakTime = store.getBreakTime();
            this.avgCostPerPerson = store.getAvgCostPerPerson();
            this.avgVisitCount = store.getAvgVisitCount();
            this.storeImg = store.getStoreImg();
            this.numsOfReview = store.getNumsOfReview();
            this.ratingAvg = store.getRatingAvg();

        }
    }


}