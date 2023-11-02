package com.ktc.matgpt.store;

import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;
import com.ktc.matgpt.like.likeStore.LikeStoreService;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import com.ktc.matgpt.utils.CursorRequest;
import com.ktc.matgpt.utils.PageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public PageResponse<Double, StoreResponse.FindAllStoreDTO> findAllByDistance(double latitude, double longitude, Double cursor, Long lastId, int size) {
        validateLatLonBoundary(List.of(latitude, longitude));
        Pageable pageable = PageRequest.of(0, size);
        List<Store> stores = getStoreListByDistance(latitude, longitude, cursor, lastId, pageable);
        return findAllStores(store -> store.calculateDistanceFromLatLon(latitude, longitude), stores, size);
    }

    // 인기 많은 음식점 보기 (리뷰많은순정렬)
    @Transactional(readOnly = true)
    public PageResponse<Integer, StoreResponse.FindAllStoreDTO> findByHighestReviewCount(Integer cursor, Long lastId, int size) {
        Pageable pageable = PageRequest.of(0, size);
        List<Store> stores = getStoreListByReviews("", cursor, lastId, pageable);
        return findAllStores(Store::getNumsOfReview, stores, size);
    }

    //비슷한 사용자들이 좋아하는 음식점 보기
    //TODO : 예외처리 해주기
    @Transactional(readOnly = true)
    public PageResponse<Long, StoreResponse.FindAllStoreDTO> getMostLikedStoresBySimilarUsers(String email, Long cursor, int size) {
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
        Collections.sort(stores,(v1,v2) -> (storeHashMap.get(v2).compareTo(storeHashMap.get(v1))));

        return findAllStores(Store::getId, stores, size);
    }

    // 음식점 검색
    @Transactional(readOnly = true)
    public PageResponse<? extends Comparable<?>, StoreResponse.FindAllStoreDTO> findBySearch(String search, Long cursor, Long lastId, int size, String sort) {
        Pageable pageable = PageRequest.of(0, size);
        List<Store> stores;
        switch (sort) {
            case "id" -> { // 높을수록 우선순위
                stores = getStoreListById(search, cursor, pageable);
                return findAllStores(Store::getId, stores, size);
            }
            case "rating" -> { // 높을수록 우선순위
                stores = getStoreListByStar(search, cursor, lastId, pageable);
                return findAllStores(Store::getRatingAvg, stores, size);
            }
            case "review" -> { // 높을수록 우선순위
                stores = getStoreListByReviews(search, Math.toIntExact(cursor), lastId, pageable);
                return findAllStores(Store::getNumsOfReview, stores, size);
            }
            default -> throw new IllegalArgumentException(String.format("sort : %s 지원하지 않는 정렬 기준입니다.", sort));
        }
    }

    public List<StoreResponse.FindAllDTO> findAllForGpt() {
        return storeJPARepository.findAll().stream()
                .map(StoreResponse.FindAllDTO::new)
                .toList();
    }

    public Store findById(Long id) {
        return storeJPARepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    public StoreResponse.FindByIdStoreDTO getStoreDtoById(Long id) {
        Store storePS = storeJPARepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND)
        );
        return new StoreResponse.FindByIdStoreDTO(storePS);
    }

    public int getNumsOfReviewById(Long storeId) {
        return findById(storeId).getNumsOfReview();
    }

    public Store getReferenceById(Long storeId) {
        try {
            return entityManager.getReference(Store.class, storeId);
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode.STORE_NOT_FOUND);
        }
    }

    private <T extends Comparable<T>> PageResponse<T, StoreResponse.FindAllStoreDTO> findAllStores(Function<Store, T> cursorExtractor, List<Store> stores, int size) {
        if (stores.isEmpty()) {
            return new PageResponse<>(new CursorRequest<>(null, null, size), List.of());
        }

        List<StoreResponse.FindAllStoreDTO> findAllStoreDTOS = stores.stream()
                .map(StoreResponse.FindAllStoreDTO::new)
                .toList();

        Store lastStore = stores.get(stores.size() - 1);
        T nextCursor = cursorExtractor.apply(lastStore);
        Long nextId = lastStore.getId();

        return new PageResponse<>(new CursorRequest<>(nextCursor, nextId, size), findAllStoreDTOS);
    }

    private void validateLatLonBoundary(List<Double> latlons) {
        for (double latlon: latlons) {
            if (!(0 <= latlon && latlon <= 180)) {
                throw new IllegalArgumentException("잘못된 위, 경도를 입력했습니다 : " + latlon);
            }
        }
    }

    private List<Store> getStoreListById(String search, Long cursor, Pageable pageable) {
        return cursor == -1
                ? storeJPARepository.findAllBySearchOrderByIdDesc(search, pageable)
                : storeJPARepository.findAllBySearchAndIdLessThanCursor(search, cursor, pageable);
    }

    private List<Store> getStoreListByStar(String search, Long cursor, Long lastId, Pageable pageable) {
        return cursor == -1
                ? storeJPARepository.findAllBySearchOrderByRatingDesc(search, pageable)
                : storeJPARepository.findAllBySearchAndRatingLessThanCursor(search,cursor, lastId, pageable);
    }

    private List<Store> getStoreListByReviews(String search, Integer cursor, Long lastId, Pageable pageable) {
        return cursor == -1
            ? storeJPARepository.findAllBySearchOrderByNumsOfReviewDesc(search, pageable)
            : storeJPARepository.findAllBySearchAndNumsOfReviewLessThanCursor(search, cursor, lastId, pageable);
    }

    private List<Store> getStoreListByDistance(double latitude, double longitude, Double cursor, Long lastId, Pageable pageable) {
        return cursor == -1.0
                ? storeJPARepository.findAllByNearest(latitude, longitude, pageable)
                : storeJPARepository.findAllByNearestAndDistanceLessThanCursor(latitude, longitude, cursor, lastId, pageable);
    }
}
