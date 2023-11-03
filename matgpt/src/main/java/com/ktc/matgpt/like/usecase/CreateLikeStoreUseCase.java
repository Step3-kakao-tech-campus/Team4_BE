package com.ktc.matgpt.like.usecase;

import com.ktc.matgpt.like.likeStore.LikeStoreService;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreService;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateLikeStoreUseCase {

    private final UserService userService;
    private final LikeStoreService likeStoreService;
    private final StoreService storeService;

    public boolean execute(Long storeId, String userEmail) {
        User userRef = userService.getReferenceByEmail(userEmail);
        Store storeRef = storeService.getReferenceById(storeId);
        return likeStoreService.toggleHeartForStore(userRef, storeRef);
    }
}
