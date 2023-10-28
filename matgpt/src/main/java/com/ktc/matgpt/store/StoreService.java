package com.ktc.matgpt.store;


import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreJPARepository storeJPARepository;
    private final EntityManager entityManager;

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
