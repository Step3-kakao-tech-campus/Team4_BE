package com.ktc.matgpt.store;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreJPARepository storeJPARepository;


    public List<StoreResponse.FindAllStoreDTO> findAll() {
        List<Store> stores = storeJPARepository.findAll();
        List<StoreResponse.FindAllStoreDTO> responseDTOs = stores.stream()
                .map(s -> new StoreResponse.FindAllStoreDTO(s))
                        .collect(Collectors.toList());
        return responseDTOs;
    }


//    public List<StoreResponse.FindAllStoreDTO> findAllByDistance(double latitude, double longitude) {
//        List<Store> stores = storeJPARepository.findNearestStoresWithDistance(latitude,longitude);
//        List<StoreResponse.FindAllStoreDTO> responseDTOs = stores.stream()
//                .map(s -> new StoreResponse.FindAllStoreDTO(s))
//                .collect(Collectors.toList());
//        return responseDTOs;
//    }

    @Transactional(readOnly = true)
    public List<StoreResponse.FindAllStoreDTO> findAllByPage(String sort,Long cursor, Pageable pageable) {

        List<Store> stores = null;
        if (sort.equals("id")){
            stores = getStoreListById(cursor,pageable);
        } else if ( sort.equals("rating")) {
            stores = getStoreListByStar(cursor,pageable);
        } else if ( sort.equals("review")) {
            stores = getStoreListByReviews(cursor,pageable);
        }

        List<StoreResponse.FindAllStoreDTO> responseDTOs = stores.stream()
                .map(s -> new StoreResponse.FindAllStoreDTO(s))
                .collect(Collectors.toList());
        return responseDTOs;
    }

    private List<Store> getStoreListById(Long id, Pageable page) {
        return id.equals(0L)
                ? storeJPARepository.findAllById(page)
                : storeJPARepository.findByIdLessThanOrderByIdDesc(id,page);

    }
    private List<Store> getStoreListByStar(Long id, Pageable page) {
        return id.equals(0L)
                ? storeJPARepository.findAllByStar(page)
                : storeJPARepository.findAllByStarLessThanIdDesc(id,page);
    }

    private List<Store> getStoreListByReviews(Long id, Pageable page) {
        return id.equals(0L)
                ? storeJPARepository.findAllByReviews(page)
                : storeJPARepository.findAllByReviewsLessThanIdDesc(id,page);
    }


    public Store findById(Long id) {
        Store store = storeJPARepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException ("해당 매장을 찾을 수 없습니다.")
        );
        return store;
    }

    public StoreResponse.FindByIdStoreDTO getStoreDtoById(Long id) {
        Store storePS = storeJPARepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException ("해당 매장을 찾을 수 없습니다.")
        );
        return new StoreResponse.FindByIdStoreDTO(storePS);
    }
}
