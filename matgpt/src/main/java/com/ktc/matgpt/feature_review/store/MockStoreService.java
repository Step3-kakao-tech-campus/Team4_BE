package com.ktc.matgpt.feature_review.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MockStoreService {
    private final MockStoreJPARepository mockStoreJPARepository;
    public MockStore findById(Long id) {
        return mockStoreJPARepository.findById(id).orElseThrow();
    }

    public List<MockStore> findAll() {
        return mockStoreJPARepository.findAll();
    }
}
