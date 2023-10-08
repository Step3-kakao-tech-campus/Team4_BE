package com.ktc.matgpt.feature_review.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreJPARepository storeJPARepository;
    public Store findById(Long id) {
        return storeJPARepository.findById(id).orElseThrow();
    }

    public List<Store> findAll() {
        return storeJPARepository.findAll();
    }
}
