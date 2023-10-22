package com.ktc.matgpt.store;



import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private static final int MY_PAGE_DEFAULT_SIZE = 4;


    @GetMapping("/stores/all")
    public ResponseEntity<?> findAll(@RequestParam(value ="lati") double latitude ,
                                     @RequestParam(value = "longi") double longitude ,
                                     @RequestParam(value = "sort", defaultValue = "id") String sort,
                                     @RequestParam(value = "cursor", defaultValue = "6") Long cursor) {
        List<StoreResponse.FindAllStoreDTO> responseDTOs = storeService.findAllByDistance(latitude,longitude);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }


    //인기 많은 음식점 - 리뷰 갯수 순
    @GetMapping("/stores/popular")
    public ResponseEntity<?> findAllPopular(@RequestParam(value = "cursor", defaultValue = "6") Long cursor) {
        List<StoreResponse.FindAllStoreDTO> responseDTOs = storeService.findAllByPopular(cursor, PageRequest.of(0, PAGE_DEFAULT_SIZE));
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }

    //최근 리뷰가 달린 음식점
    @GetMapping("/stores/recent")
    public ResponseEntity<?> findAllRecent(@RequestParam(value = "cursor", defaultValue = "6") Long cursor) {
        //리뷰에서 가져와야함
        return ResponseEntity.ok(null);
    }

    //비슷한 사용자들이 좋아하는 음식점
    @GetMapping("/stores/similar")
    public ResponseEntity<?> findAllSimilar(@RequestParam(value = "cursor", defaultValue = "6") Long cursor , @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<StoreResponse.FindAllStoreDTO> responseDTOs = null;
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }


    //음식점 검색
    @GetMapping("/stores")
    public ResponseEntity<?> Search( @RequestParam(value ="lati") double latitude ,
                                     @RequestParam(value = "longi") double longitude ,
                                     @RequestParam(value = "sort", defaultValue = "id") String sort,
                                     @RequestParam(value = "cursor", defaultValue = "6") Long cursor,
                                     @RequestParam(value="q") String searchQuery ) {
        List<StoreResponse.FindAllStoreDTO> responseDTOs = storeService.findBySearch(searchQuery,sort,cursor, PageRequest.of(0, PAGE_DEFAULT_SIZE));
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }



    @GetMapping("/stores/{id}")
    public ResponseEntity<?> findById(@PathVariable(value = "id", required = true) Long id) {
        StoreResponse.FindByIdStoreDTO responseDTO = storeService.getStoreDtoById(id);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTO);
        return ResponseEntity.ok(apiResult);
    }

}