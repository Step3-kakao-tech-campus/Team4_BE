package com.ktc.matgpt.store;

import com.ktc.matgpt.food.Food;
import com.ktc.matgpt.food.FoodService;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import com.ktc.matgpt.utils.PageResponse;
import lombok.RequiredArgsConstructor;
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


    //마커 찍는 stores 보내주기
    @GetMapping("")
    public ResponseEntity<?> markerStores(@RequestParam(value = "maxlat") double maxLat ,
                                          @RequestParam(value = "maxlon") double maxLon ,
                                          @RequestParam(value = "minlat") double minLat ,
                                          @RequestParam(value = "minlon") double minLon) {
        List<StoreResponse.MarkerStoresDTO> responseDTOs = storeService.findAllMarkers(maxLat, maxLon, minLat, minLon);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }

    //사용자 거리와 가까운 순으로 불러오기
    // TODO : 커서 기반 페이지네이션
    @GetMapping("/all")
    public ResponseEntity<?> findAll(@RequestParam(value = "lati") double latitude ,
                                     @RequestParam(value = "longi") double longitude ,
                                     @RequestParam(value = "cursor", required = false, defaultValue = "-1.0") Double cursor,
                                     @RequestParam(value = "lastid", required = false) Long lastId) {
        PageResponse<Double, StoreResponse.FindAllStoreDTO> responseDTOs = storeService.findAllByDistance(latitude, longitude, cursor, lastId, PAGE_DEFAULT_SIZE);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTOs);
        return ResponseEntity.ok(apiResult);
    }

    //음식점 검색
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "sort", defaultValue = "id") String sort,
                                    @RequestParam(value = "cursor", required = false, defaultValue = "-1") Long cursor,
                                    @RequestParam(value = "lastid", required = false) Long lastId,
                                    @RequestParam(value= "q") String searchQuery ) {
        PageResponse<?, StoreResponse.FindAllStoreDTO> pageResponse = storeService.findBySearch(searchQuery, cursor, lastId, PAGE_DEFAULT_SIZE, sort);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(pageResponse);
        return ResponseEntity.ok(apiResult);
    }

    //인기 많은 음식점 - 리뷰 갯수 순
    @GetMapping("/popular")
    public ResponseEntity<?> findAllByPopular(@RequestParam(value = "cursor", required = false, defaultValue = "-1") Integer cursor,
                                              @RequestParam(value = "lastid", required = false) Long lastId) {
        PageResponse<Integer, StoreResponse.FindAllStoreDTO> pageResponse = storeService.findAllByPopular(cursor, lastId, PAGE_DEFAULT_SIZE);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(pageResponse);
        return ResponseEntity.ok(apiResult);
    }

    // 최근 리뷰가 달린 음식점
//    @GetMapping("/recent-review")
//    public ResponseEntity<?> findAllByRecentReviews(@RequestParam(value = "cursor", required = false) LocalDateTime cursor,
//                                           @RequestParam(value = "lastid", required = false) Long lastId) {
//        PageResponse<LocalDateTime, StoreResponse.FindAllStoreDTO> pageResponse = storeService.findAllByRecentReviews(cursor, lastId, PAGE_DEFAULT_SIZE);
//        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(pageResponse);
//        return ResponseEntity.ok(apiResult);
//    }

    //비슷한 사용자들이 좋아하는 음식점
    @GetMapping("/similar")
    public ResponseEntity<?> findAllBySimilar(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @RequestParam(value = "cursor", required = false, defaultValue = "-1") Long cursor) {
        PageResponse<Long, StoreResponse.FindAllStoreDTO> pageResponse = storeService.findSimilarStores(userPrincipal.getEmail(), cursor, PAGE_DEFAULT_SIZE);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(pageResponse);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<?> findById(@PathVariable(value = "storeId") Long storeId) {
        StoreResponse.FindByIdStoreDTO responseDTO = storeService.getStoreDtoById(storeId);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(responseDTO);
        return ResponseEntity.ok(apiResult);
    }

    // 검색 기록을 통해 해당 음식점에서 최근 검색된 음식 리스트를 반환하는 로직
    // 예시로는 단순히 최근에 생성된 음식을 반환합니다.
    // TODO : 오류 해결
    @GetMapping("/{storeId}/recentlySearchedFoods")
    public ResponseEntity<List<Food>> getRecentlySearchedFoodsByStore(@PathVariable Long storeId) {
        List<Food> recentlySearchedFoods = foodService.getRecentlySearchedFoodsByStore(storeId);
        return ResponseEntity.ok(recentlySearchedFoods);
    }
}
