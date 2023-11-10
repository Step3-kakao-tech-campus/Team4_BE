package com.ktc.matgpt.domain.store;

import com.ktc.matgpt.domain.review.dto.ReviewResponse;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.domain.store.usecase.GetRecentlyReviewedStoresUseCase;
import com.ktc.matgpt.utils.ApiUtils;
import com.ktc.matgpt.utils.paging.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stores")
public class StoreRestController {

    private final StoreService storeService;
    private final GetRecentlyReviewedStoresUseCase getRecentlyReviewedStoresUseCase;

    @GetMapping("/{storeId}")
    public ResponseEntity<?> findById(@PathVariable Long storeId) {
        StoreResponse.FindByIdStoreDTO responseDTO = storeService.getStoreDtoById(storeId);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }

    //마커 찍는 stores 보내주기
    @GetMapping("/boundary")
    public ResponseEntity<?> getMarkedStores(@RequestParam(value = "maxlat", defaultValue = "180") double maxLat,
                                             @RequestParam(value = "maxlon", defaultValue = "180") double maxLon,
                                             @RequestParam(value = "minlat", defaultValue = "0") double minLat,
                                             @RequestParam(value = "minlon", defaultValue = "0") double minLon) {
        List<StoreResponse.MarkerStoresDTO> responseDTOs = storeService.findAllMarkers(maxLat, maxLon, minLat, minLon);
        return ResponseEntity.ok(ApiUtils.success(responseDTOs));
    }

    //사용자 거리와 가까운 순으로 불러오기
    @GetMapping("/nearest")
    public ResponseEntity<?> findNearestStores(@RequestParam(value = "lati") double latitude,
                                               @RequestParam(value = "longi") double longitude,
                                               @RequestParam(required = false) Double cursor,
                                               @RequestParam(value = "cursorid", required = false) Long cursorId) {
        PageResponse<Double, StoreResponse.FindAllStoreDTO> responseDTOs = storeService.findAllByDistance(latitude, longitude, cursor, cursorId);
        return ResponseEntity.ok(ApiUtils.success(responseDTOs));
    }

    //음식점 검색
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "sort", defaultValue = "id") String sort,
                                    @RequestParam(value= "q", required = false) String searchQuery,
                                    @RequestParam(required = false) Long cursor,
                                    @RequestParam(value = "cursorid", required = false) Long cursorId) {
        PageResponse<?, StoreResponse.FindAllStoreDTO> pageResponse = storeService.findBySearch(searchQuery, cursor, cursorId, sort);
        return ResponseEntity.ok(ApiUtils.success(pageResponse));
    }

    //인기 많은 음식점 - 리뷰 갯수 순
    @GetMapping("/popular")
    public ResponseEntity<?> findByHighestReviewCount(@RequestParam(required = false) Integer cursor,
                                                      @RequestParam(value = "cursorid", required = false) Long cursorId) {
        PageResponse<Integer, StoreResponse.FindAllStoreDTO> pageResponse = storeService.findByHighestReviewCount(cursor, cursorId);
        return ResponseEntity.ok(ApiUtils.success(pageResponse));
    }

    // 비슷한 사용자들이 좋아하는 음식점 5개
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/similar")
    public ResponseEntity<?> getMostLikedStoresBySimilarUsersTop5(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<StoreResponse.FindAllStoreDTO> pageResponse = storeService.getMostLikedStoresBySimilarUsersTop5(userPrincipal.getEmail());
        return ResponseEntity.ok(ApiUtils.success(pageResponse));
    }

    @GetMapping("/recent-reviews")
    public ResponseEntity<?> getRecentlyReviewedStores(@RequestParam(required = false) LocalDateTime cursor,
                                                       @RequestParam(value = "cursorid", required = false) Long cursorId) {
        PageResponse<LocalDateTime, ReviewResponse.RecentReviewDTO> recentlyReviewedStores = getRecentlyReviewedStoresUseCase.execute(cursorId, cursor);
        return ResponseEntity.ok(ApiUtils.success(recentlyReviewedStores));
    }
}
