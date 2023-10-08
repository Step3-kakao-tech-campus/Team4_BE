package com.ktc.matgpt.feature_review.store;

import com.ktc.matgpt.feature_review.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class StoreRestController {
    private final StoreService storeService;

    @GetMapping("/stores/{id}")
    public ResponseEntity<?> find(@PathVariable(value = "id", required = true) Long id) {
        Store store = storeService.findById(id);

        StoreResponse.builder()
                .storeId(store.getId())
                .storeName(store.getStoreName())
                .build();
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(new StoreResponse(store.getId(), store.getStoreName()));
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/stores")
    public ResponseEntity<?> findAll() {
        List<Store> store = storeService.findAll();

        List<StoreResponse> responseDTOs = store.stream()
                .map(s -> StoreResponse.builder()
                        .storeId(s.getId())
                        .storeName(s.getStoreName())
                        .build())
                .collect(Collectors.toList());

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }

}
