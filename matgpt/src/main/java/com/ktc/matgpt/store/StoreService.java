package com.ktc.matgpt.store;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreJPARepository storeJPARepository;
    public Store findById(Long id) {
        return storeJPARepository.findById(id).orElseThrow();
    }

    public List<StoreResponse.FindAllStoreDTO> findAll() {
        List<Store> store = storeJPARepository.findAll();

        List<StoreResponse.FindAllStoreDTO> responseDTOs = store.stream()
                .map(s -> new StoreResponse.FindAllStoreDTO(s))
                        .collect(Collectors.toList());
        return responseDTOs;
    }

}
