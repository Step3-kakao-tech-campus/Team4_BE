package com.ktc.matgpt.domain.store;

import com.ktc.matgpt.exception.ErrorMessage;
import com.ktc.matgpt.domain.like.likeStore.LikeStoreService;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.domain.user.service.UserService;
import com.ktc.matgpt.utils.paging.PageResponse;
import com.ktc.matgpt.utils.paging.Paging;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final UserService userService;
    private final LikeStoreService likeStoreService;
    private final StoreJPARepository storeJPARepository;
    private final EntityManager entityManager;
    private static final int PAGE_SIZE_PLUS_ONE = 5 + 1;

    // 마커 표시할 음식점 보기
    @Transactional(readOnly = true)
    public List<StoreResponse.MarkerStoresDTO> findAllMarkers(double maxLat, double maxLon, double minLat, double minLon) {
        validateLatLonBoundary(List.of(maxLat, maxLon, minLat, minLon));

        List<Store> stores = storeJPARepository.findAllWithinLatLonBoundaries(maxLat, maxLon, minLat, minLon);
        return stores.stream()
                .map(StoreResponse.MarkerStoresDTO::new)
                .toList();
    }

    // 사용자에게 가까운 순으로 보기 + 커서 기반 페이지네이션
    @Transactional(readOnly = true)
    public PageResponse<Double, StoreResponse.FindAllStoreDTO> findAllByDistance(double latitude, double longitude, Double cursor, Long lastId) {
        // 가까운 순이므로 거리 최솟값이 cursor
        cursor = Paging.convertNullCursorToMinValue(cursor);
        validateLatLonBoundary(List.of(latitude, longitude));
        Pageable pageable = Pageable.ofSize(PAGE_SIZE_PLUS_ONE);
        List<Store> stores = storeJPARepository.findAllByNearestAndDistanceLessThanCursor(latitude, longitude, cursor, lastId, pageable);

        return extractCursorAndGetPageResponse(store -> store.calculateDistanceFromLatLon(latitude, longitude), stores);
    }

    // 리뷰 많은 순으로 음식점 보기 + 커서 기반 페이지네이션
    @Transactional(readOnly = true)
    public PageResponse<Integer, StoreResponse.FindAllStoreDTO> findByHighestReviewCount(Integer cursor, Long lastId) {
        cursor = Paging.convertNullCursorToMaxValue(cursor);
        Pageable pageable = Pageable.ofSize(PAGE_SIZE_PLUS_ONE);
        List<Store> stores = getStoreListByReviews("", cursor, lastId, pageable);

        return extractCursorAndGetPageResponse(Store::getNumsOfReview, stores);
    }

    //비슷한 사용자들이 좋아하는 음식점 보기
    @Transactional(readOnly = true)
    public List<StoreResponse.FindAllStoreDTO> getMostLikedStoresBySimilarUsersTop5(String email) {
        User user = userService.findByEmail(email);
        List<User> similarUserList = userService.findByAgeGroupAndGender(user.getAgeGroup(), user.getGender());

        //다른 사용자들이 좋아요 누른 음식점 list counting
        Map<Store, Integer> storeHashMap = new HashMap<>();
        for (User similarUser : similarUserList){
            List<Store> stores = likeStoreService.findLikedStoresByUserId(similarUser.getId());
            for (Store store : stores) {
                if (!storeHashMap.containsKey(store) ){
                    storeHashMap.put(store, 1);
                } else {
                    int count = storeHashMap.get(store);
                    count += 1;
                    storeHashMap.put(store, count);
                }
            }
        }

        //내림차순 정렬
        List<Store> stores = new ArrayList<>(storeHashMap.keySet());
        Collections.sort(stores,(v1, v2) -> (storeHashMap.get(v2).compareTo(storeHashMap.get(v1))));

        return stores.stream()
                .map(StoreResponse.FindAllStoreDTO::new)
                .limit(5)
                .toList();
    }

    // 음식점 검색 + 커서 기반 페이지네이션
    @Transactional(readOnly = true)
    public PageResponse<? extends Comparable<?>, StoreResponse.FindAllStoreDTO> findBySearch(String search, Long cursor, Long lastId, String sort) {
        cursor = Paging.convertNullCursorToMaxValue(cursor);
        Pageable pageable = Pageable.ofSize(PAGE_SIZE_PLUS_ONE);
        List<Store> stores;
        switch (sort) {
            case "id" -> { // 높을수록 우선순위
                stores = storeJPARepository.findAllBySearchAndIdLessThanCursor(search, cursor, pageable);
                return extractCursorAndGetPageResponse(Store::getId, stores);
            }
            case "rating" -> { // 높을수록 우선순위
                stores = storeJPARepository.findAllBySearchAndRatingLessThanCursor(search, cursor, lastId, pageable);
                return extractCursorAndGetPageResponse(Store::getAvgRating, stores);
            }
            case "review" -> { // 높을수록 우선순위
                stores = getStoreListByReviews(search, Math.toIntExact(cursor), lastId, pageable);
                return extractCursorAndGetPageResponse(Store::getNumsOfReview, stores);
            }
            default -> throw new IllegalArgumentException(ErrorMessage.INVALID_SORT_TYPE);
        }
    }

    public List<StoreResponse.FindAllDTO> findAllForGpt() {
        return storeJPARepository.findAll().stream()
                .map(StoreResponse.FindAllDTO::new)
                .toList();
    }

    public Store findById(Long id) {
        return storeJPARepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.STORE_NOT_FOUND));
    }

    public StoreResponse.FindByIdStoreDTO getStoreDtoById(Long id) {
        Store storePS = storeJPARepository.findById(id).orElseThrow(
                () ->  new NoSuchElementException(ErrorMessage.STORE_NOT_FOUND));
        return new StoreResponse.FindByIdStoreDTO(storePS);
    }

    public int getNumsOfReviewById(Long storeId) {
        return findById(storeId).getNumsOfReview();
    }

    public Store getReferenceById(Long storeId) {
        try {
            return entityManager.getReference(Store.class, storeId);
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException(ErrorMessage.STORE_NOT_FOUND);
        }
    }

    private <T extends Comparable<T>> PageResponse<T, StoreResponse.FindAllStoreDTO> extractCursorAndGetPageResponse(Function<Store, T> cursorExtractor, List<Store> stores) {
        if (stores.isEmpty()) {
            return new PageResponse<>(new Paging<>(false, 0, null, null), List.of());
        }

        int size = stores.size();
        boolean hasNext = false;
        if (size == PAGE_SIZE_PLUS_ONE) {
            stores.remove(size - 1);
            size -= 1;
            hasNext = true;
        }

        List<StoreResponse.FindAllStoreDTO> findAllStoreDTOS = stores.stream()
                .map(StoreResponse.FindAllStoreDTO::new)
                .toList();

        Store lastStore = stores.get(size - 1);
        T nextCursor = cursorExtractor.apply(lastStore);
        Long nextCursorId = lastStore.getId();

        return new PageResponse<>(new Paging<>(hasNext, size, nextCursor, nextCursorId), findAllStoreDTOS);
    }

    private void validateLatLonBoundary(List<Double> latlons) {
        for (double latlon: latlons) {
            if (!(0 <= latlon && latlon <= 180)) {
                throw new IllegalArgumentException("잘못된 위, 경도를 입력했습니다 : " + latlon);
            }
        }
    }
    private List<Store> getStoreListByReviews(String search, Integer cursor, Long lastId, Pageable pageable) {
        return storeJPARepository.findAllBySearchAndNumsOfReviewLessThanCursor(search, cursor, lastId, pageable);
    }
}
