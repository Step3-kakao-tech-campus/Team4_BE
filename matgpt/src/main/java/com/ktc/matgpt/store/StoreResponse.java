package com.ktc.matgpt.store;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreResponse {
    private Long storeId;
    private String storeName;
    @Builder
    public StoreResponse(Long storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }
}