package com.ktc.matgpt.store;



import com.ktc.matgpt.utils.ApiUtils;
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



    @GetMapping("/stores")
    public ResponseEntity<?> findAll() {
        List<StoreResponse.FindAllStoreDTO> responseDTOs = storeService.findAll();
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/stores/{id}")
    public ResponseEntity<?> find(@PathVariable(value = "id", required = true) Long id) {
        Store store = storeService.findById(id);

        StoreResponse.builder()
                .storeId(store.getId())
                .storeName(store.getStoreName())
                .build();
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(new StoreResponse(store.getId(), store.getStoreName()));
        return ResponseEntity.ok(apiResult);
    }

}
