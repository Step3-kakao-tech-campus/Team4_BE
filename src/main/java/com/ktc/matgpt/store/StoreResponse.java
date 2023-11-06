package com.ktc.matgpt.store;

import com.ktc.matgpt.store.entity.Category;
import com.ktc.matgpt.store.entity.SubCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
public class StoreResponse {

    @Getter
    @Setter
    public static class FindAllDTO {
        private Long storeId;
        private String storeName;
        private int numsOfReview;

        public FindAllDTO(Store store) {
            this.storeId = store.getId();
            this.storeName = store.getName();
            this.numsOfReview = store.getNumsOfReview();
        }
    }

    @Getter
    @Setter
    public static class MarkerStoresDTO{
        private Long storeId;
        private String storeName;
        private double latitude;
        private double longitude;
        private String storeImage;

        public MarkerStoresDTO(Store store){
            this.storeId = store.getId();
            this.storeName = store.getName();
            this.latitude = store.getLatitude();
            this.longitude = store.getLongitude();
            this.storeImage = store.getStoreImageUrl();
        }
    }

    @Getter
    @Setter
    public static class FindAllStoreDTO {
        private Long storeId;
        private String storeName;
        private CategoryDTO category;
        private double ratingAvg;
        private int numsOfReview;

        public FindAllStoreDTO(Store store) {
            this.storeId = store.getId();
            this.storeName = store.getName();
            this.category = new CategoryDTO(store.getSubCategory().getCategory());
            this.ratingAvg = store.getRatingAvg();
            this.numsOfReview = store.getNumsOfReview();
        }

        @Getter
        @Setter
        public class CategoryDTO{
            private Long id;
            private String name;

            public CategoryDTO(Category category){
                this.id = category.getId();
                this.name = category.getName();

            }
        }
    }

    @Getter
    @Setter
    public static class FindByIdStoreDTO {

        private Long storeId;
        private String storeName;
        private SubCategory subCategory;
        private String phoneNumber;
        private String address;
        private String businessHours;
        private double avgCostPerPerson;
        private int avgVisitCount;
        private String storeImg;
        private int numsOfReview;
        private double ratingAvg;

        public FindByIdStoreDTO(Store store) {
            this.storeId = store.getId();
            this.storeName = store.getName();
            this.subCategory = store.getSubCategory();
            this.phoneNumber = store.getPhoneNumber();
            this.address = store.getAddress();
            this.businessHours = store.getBusinessHours();
            this.avgCostPerPerson = store.getAvgCostPerPerson();
            this.avgVisitCount = store.getAvgVisitCount();
            this.storeImg = store.getStoreImageUrl();
            this.numsOfReview = store.getNumsOfReview();
            this.ratingAvg = store.getRatingAvg();
        }
    }
}
