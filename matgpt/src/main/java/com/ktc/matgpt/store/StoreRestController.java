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
        StoreResponse.FindByIdStoreDTO responseDTO = storeService.findById(id);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTO);
        return ResponseEntity.ok(apiResult);
    }

}
