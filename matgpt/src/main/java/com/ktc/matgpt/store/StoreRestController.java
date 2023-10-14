package com.ktc.matgpt.store;



import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class StoreRestController {
    private final StoreService storeService;
    private static final int PAGE_DEFAULT_SIZE = 5;


    @GetMapping("/stores")
    public ResponseEntity<?> findAllByPage(@RequestParam(value = "sort", defaultValue = "id") String sort,
                                           @RequestParam(value = "cursor", defaultValue = "6") Long cursor) {
        List<StoreResponse.FindAllStoreDTO> responseDTOs = storeService.findAllByPage(sort,cursor, PageRequest.of(0, PAGE_DEFAULT_SIZE));
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }


//    @GetMapping("/stores/dis")
//    public ResponseEntity<?> findAllbyDis( @RequestParam("user_latitude") double latitude,
//                                           @RequestParam("user_longitude") double longitude) {
//        List<StoreResponse.FindAllStoreDTO> responseDTOs = storeService.findAllByDistance(latitude,longitude);
//        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTOs);
//        return ResponseEntity.ok(apiResult);
//    }


    @GetMapping("/stores/{id}")
    public ResponseEntity<?> findById(@PathVariable(value = "id", required = true) Long id) {
        StoreResponse.FindByIdStoreDTO responseDTO = storeService.findById(id);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTO);
        return ResponseEntity.ok(apiResult);
    }

}
