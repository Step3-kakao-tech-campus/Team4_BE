package com.ktc.matgpt.domain.like.likeStore;

import com.ktc.matgpt.domain.store.Store;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.domain.user.service.UserService;
import com.ktc.matgpt.utils.paging.PageResponse;
import com.ktc.matgpt.utils.paging.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeStoreService {

    private static final int DEFAULT_PAGE_SIZE = 6 ;

    private final UserService userService;
    private final LikeStoreJPARepository likeStoreJPARepository;

    @Transactional
    public boolean toggleHeartForStore(User userRef, Store storeRef) {
        if (isHeartAlreadyExists(userRef, storeRef)) {
            deleteHeartToStore(userRef, storeRef);
            return false;
        } else {
            addHeartToStore(userRef, storeRef);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public PageResponse<Long, LikeStoreResponseDTO.FindAllLikeStoresDTO> findLikeStoresByUserEmail(String email, Long cursorId) {
        User userRef = userService.getReferenceByEmail(email);
        cursorId = Paging.convertNullCursorToMaxValue(cursorId);
        Pageable page = PageRequest.ofSize(DEFAULT_PAGE_SIZE+1);

        List<LikeStore> likeStoreList = likeStoreJPARepository.findLikedStoresByUserId(userRef.getId(), cursorId, page);
        if (likeStoreList.isEmpty()) {
            return new PageResponse<>(new Paging<>(false, 0, null, null), null);
        }

        Paging paging = getPagingInfo(likeStoreList);
        likeStoreList = likeStoreList.subList(0, paging.size());

        return new PageResponse<>(paging, likeStoreList.stream().map(likeStore ->
                        new LikeStoreResponseDTO.FindAllLikeStoresDTO(likeStore.getStore()))
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public List<Store> findLikedStoresByUserId(Long userId) {
        return likeStoreJPARepository.findLikedStoresByUserId(userId);
    }

    private void addHeartToStore(User userRef, Store storeRef){
        likeStoreJPARepository.save(LikeStore.create(userRef,storeRef));
    }

    private void deleteHeartToStore(User userRef, Store storeRef) {
        likeStoreJPARepository.deleteByUserAndStore(userRef, storeRef);
    }

    private boolean isHeartAlreadyExists(User userRef, Store storeRef) {
        return likeStoreJPARepository.existsByUserAndStore(userRef, storeRef);
    }

    private Paging<Long> getPagingInfo(List<LikeStore> likeStoreList) {
        boolean hasNext = false;
        int numsOfStores = 0;

        if (likeStoreList.size() == DEFAULT_PAGE_SIZE+1) {
            hasNext = true;
            numsOfStores = DEFAULT_PAGE_SIZE;
        } else {
            numsOfStores = likeStoreList.size();
        }

        Long nextCursorId = likeStoreList.get(numsOfStores-1).getId();
        return new Paging<Long>(hasNext, numsOfStores, nextCursorId, nextCursorId);
    }
}
