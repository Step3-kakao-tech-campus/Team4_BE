package com.ktc.matgpt.like.likeStore;


import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreService;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeStoreService {

    private final StoreService storeService;
    private final UserService userService;
    private final LikeStoreJPARepository likeStoreJPARepository;

    private final EntityManager entityManager;

    @Transactional
    public boolean toggleHeartForStore(Long storeId, String email) {

        User userRef = userService.getReferenceByEmail(email);
        Store storeRef = storeService.getReferenceById(storeId);

        if (isHeartAlreadyExists(userRef, storeRef)) {
            deleteHeartToStore(userRef, storeRef);
            return false;
        } else {
            addHeartToStore(userRef, storeRef);
            return true;
        }
    }
    public LikeStoreResponseDTO.FindAllLikeStoresDTO findStoresByUserEmail(String email) {
        User user = userService.findByEmail(email);
        List<Store> storeList=  likeStoreJPARepository.findLikedStoresByUserId(user.getId()).stream().toList();
        return new LikeStoreResponseDTO.FindAllLikeStoresDTO(user,storeList);
    }

    @Transactional
    public void addHeartToStore(User userRef, Store storeRef){
        likeStoreJPARepository.save(LikeStore.create(userRef,storeRef));
    }

    @Transactional
    public void deleteHeartToStore(User userRef, Store storeRef) {
        likeStoreJPARepository.deleteByUserAndStore(userRef, storeRef);
    }

    private boolean isHeartAlreadyExists(User userRef, Store storeRef) {
        return likeStoreJPARepository.existsByUserAndStore(userRef, storeRef);
    }

}
