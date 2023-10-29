package com.ktc.matgpt.store;


import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;
import com.ktc.matgpt.like.likeStore.LikeStore;
import com.ktc.matgpt.like.likeStore.LikeStoreJPARepository;
import com.ktc.matgpt.user.entity.AgeGroup;
import com.ktc.matgpt.user.entity.Gender;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.repository.UserRepository;
import com.ktc.matgpt.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreJPARepository storeJPARepository;
    private final UserService userService;
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final LikeStoreJPARepository likeStoreJPARepository;

    //마커 표시할 음식점 보기
    @Transactional(readOnly = true)
    public List<StoreResponse.MarkerStoresDTO> findAllMarkers(double maxLatitude, double maxLongitude,double minLatitude, double minLongitude) {

        List<Store> stores = storeJPARepository.findMarkers(maxLatitude,maxLongitude,minLatitude,minLongitude);

        List<StoreResponse.MarkerStoresDTO> responseDTOs = stores.stream()
                .map(s -> new StoreResponse.MarkerStoresDTO(s))
                .collect(Collectors.toList());
        return responseDTOs;
    }


    //사용자에게 가까운 순으로 보기
    @Transactional(readOnly = true)
    public List<StoreResponse.FindAllStoreDTO> findAllByDistance(double latitude, double longitude) {
        List<Store> stores = storeJPARepository.findNearestStoresWithDistance(latitude,longitude);
        List<StoreResponse.FindAllStoreDTO> responseDTOs = stores.stream()
                .map(s -> new StoreResponse.FindAllStoreDTO(s))
                .collect(Collectors.toList());
        return responseDTOs;
    }

    //인기 많은 음식점 보기 (리뷰많은순정렬)
    @Transactional(readOnly = true)
    public List<StoreResponse.FindAllStoreDTO> findAllByPopular(Long cursor, Pageable pageable) {

        List<Store> stores =  getStoreListByReviews(cursor,pageable,"");

        List<StoreResponse.FindAllStoreDTO> responseDTOs = stores.stream()
                .map(s -> new StoreResponse.FindAllStoreDTO(s))
                .collect(Collectors.toList());
        return responseDTOs;
    }

    //비슷한 사용자들이 좋아하는 음식점 보기
    //TODO : 예외처리 해주기
    @Transactional(readOnly = true)
    public List<StoreResponse.FindAllStoreDTO> findSimilarStores(String email, Long cursor, Pageable page) {
        User user = userService.findByEmail(email);
        AgeGroup ageGroup = user.getAgeGroup();
        Gender gender = user.getGender();

        //사용자와 나이와 성별이 같은 다른 사용자들 찾기
        List<User> userList =  userRepository.findAll().stream()
                .filter(u -> u.getAgeGroup().equals(ageGroup))
                .filter(u -> u.getGender().equals(gender))
                .collect(Collectors.toList());


        //다른 사용자들이 좋아요 누른 음식점 list counting
        Map<Store,Integer> storeList = new HashMap<>();
        for (User u : userList){
            List<Store> stores = likeStoreJPARepository.findLikedStoresByUserId(u.getId());
            for (Store s : stores) {
                if ( !storeList.containsKey(s) ){
                    storeList.put(s,1);
                } else {
                    int count = storeList.get(s);
                    count += 1;
                    storeList.put(s,count);
                }
            }
        }

        //내림차순 정렬
        List<Store> stores = new ArrayList<>(storeList.keySet());
        Collections.sort(stores,(v1,v2) -> (storeList.get(v2).compareTo(storeList.get(v1))));


        //DTO로 response해주기
        List<StoreResponse.FindAllStoreDTO> responseDTOs = stores.stream()
                .map(s -> new StoreResponse.FindAllStoreDTO(s))
                .collect(Collectors.toList());

        return responseDTOs;
    }


    //음식점 검색
    @Transactional(readOnly = true)
    public  List<StoreResponse.FindAllStoreDTO> findBySearch(String searchQuery,String sort, Long cursor, Pageable pageable) {


        List<Store> stores = null;
        if (sort.equals("id")){
            stores = getStoreListById(cursor,pageable,searchQuery);
        } else if ( sort.equals("rating")) {
            stores = getStoreListByStar(cursor,pageable,searchQuery);
        } else if ( sort.equals("review")) {
            stores = getStoreListByReviews(cursor,pageable,searchQuery);
        }

        List<StoreResponse.FindAllStoreDTO> responseDTOs = stores.stream()
                .map(s -> new StoreResponse.FindAllStoreDTO(s))
                .collect(Collectors.toList());
        return responseDTOs;
    }

    public List<StoreResponse.FindAllDTO> findAll() {
        return storeJPARepository.findAll().stream()
                .map(StoreResponse.FindAllDTO::new)
                .toList();
    }


    private List<Store> getStoreListById(Long id, Pageable page , String search) {
        return id.equals(0L)
                ? storeJPARepository.findAllById(search,page)
                : storeJPARepository.findByIdLessThanOrderByIdDesc(search,id,page);

    }
    private List<Store> getStoreListByStar(Long id, Pageable page, String search) {
        return id.equals(0L)
                ? storeJPARepository.findAllByStar(search,page)
                : storeJPARepository.findAllByStarLessThanIdDesc(search,id,page);
    }

    private List<Store> getStoreListByReviews(Long id, Pageable page, String search) {
        return id.equals(0L)
                ? storeJPARepository.findAllByReviews(search,page)
                : storeJPARepository.findAllByReviewsLessThanIdDesc(search,id,page);
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


}
