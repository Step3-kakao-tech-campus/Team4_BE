package com.ktc.matgpt.store;



import com.ktc.matgpt.food.Food;
import com.ktc.matgpt.food.FoodService;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/store")
public class StoreRestController {
    private final StoreService storeService;
    private final FoodService foodService;
    private static final int PAGE_DEFAULT_SIZE = 5;
    private static final int MY_PAGE_DEFAULT_SIZE = 4;


    @GetMapping("/all")
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

    // 검색 기록을 통해 해당 음식점에서 최근 검색된 음식 리스트를 반환하는 로직
    // 예시로는 단순히 최근에 생성된 음식을 반환합니다.
    @GetMapping("/{storeId}/recentlySearchedFoods")
    public ResponseEntity<List<Food>> getRecentlySearchedFoodsByStore(@PathVariable Long storeId) {
        List<Food> recentlySearchedFoods = foodService.getRecentlySearchedFoodsByStore(storeId);
        return ResponseEntity.ok(recentlySearchedFoods);
    }

}