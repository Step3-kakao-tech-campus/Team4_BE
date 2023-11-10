package com.ktc.matgpt.domain.like.usecase;

import com.ktc.matgpt.domain.like.likeStore.LikeStoreService;
import com.ktc.matgpt.domain.store.Store;
import com.ktc.matgpt.domain.store.StoreService;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.domain.user.service.UserService;
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
