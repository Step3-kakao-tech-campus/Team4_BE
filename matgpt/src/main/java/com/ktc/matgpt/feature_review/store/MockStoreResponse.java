package com.ktc.matgpt.feature_review.store;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MockStoreResponse {
    private Long storeId;
    private String storeName;
    @Builder
    public MockStoreResponse(Long storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }
}
