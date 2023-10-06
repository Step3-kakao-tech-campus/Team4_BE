package com.ktc.matgpt.store;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreJPARepository storeJPARepository;


    public List<StoreResponse.FindAllStoreDTO> findAll() {
        List<Store> store = storeJPARepository.findAll();
        List<StoreResponse.FindAllStoreDTO> responseDTOs = store.stream()
                .map(s -> new StoreResponse.FindAllStoreDTO(s))
                        .collect(Collectors.toList());
        return responseDTOs;
    }


    public StoreResponse.FindByIdStoreDTO findById(Long id) {
        Store storePS = storeJPARepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException ("해당 매장을 찾을 수 없습니다.")
        );
        return new StoreResponse.FindByIdStoreDTO(storePS);
    }

}
