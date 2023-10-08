package com.ktc.matgpt.feature_review.store;

import com.ktc.matgpt.feature_review.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class MockStoreRestController {
    private final MockStoreService mockStoreService;

    @GetMapping("/mockstores/{id}")
    public ResponseEntity<?> findByStoreId(@PathVariable(value = "id", required = true) Long id) {
        MockStore mockStore = mockStoreService.findById(id);

        MockStoreResponse.builder()
                .storeId(mockStore.getId())
                .storeName(mockStore.getStoreName())
                .build();
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(new MockStoreResponse(mockStore.getId(), mockStore.getStoreName()));
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/mockstores")
    public ResponseEntity<?> findAll() {
        List<MockStore> mockStore = mockStoreService.findAll();

        List<MockStoreResponse> responseDTOs = mockStore.stream()
                .map(s -> MockStoreResponse.builder()
                        .storeId(s.getId())
                        .storeName(s.getStoreName())
                        .build())
                .collect(Collectors.toList());

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }

}
