package com.ktc.matgpt.like.likeStore;

import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import com.ktc.matgpt.utils.PageResponse;
import com.ktc.matgpt.utils.Paging;
import jakarta.persistence.EntityManager;
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

    private static final int DEFAULT_PAGE_SIZE = 8;

    private final UserService userService;
    private final LikeStoreJPARepository likeStoreJPARepository;

    private final EntityManager entityManager;

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

    @Transactional
    public void addHeartToStore(User userRef, Store storeRef){
        likeStoreJPARepository.save(LikeStore.create(userRef,storeRef));
    }

    @Transactional
    public void deleteHeartToStore(User userRef, Store storeRef) {
        likeStoreJPARepository.deleteByUserAndStore(userRef, storeRef);
    }

    public List<Store> findLikedStoresByUserId(Long userId) {
        return likeStoreJPARepository.findLikedStoresByUserId(userId);
    }

    private boolean isHeartAlreadyExists(User userRef, Store storeRef) {
        return likeStoreJPARepository.existsByUserAndStore(userRef, storeRef);
    }

    private Paging<Long> getPagingInfo(List<LikeStore> likeStoreList) {
        boolean hasNext = false;
        int numsOfReviews = 0;

        if (likeStoreList.size() == DEFAULT_PAGE_SIZE+1) {
            hasNext = true;
            numsOfReviews = DEFAULT_PAGE_SIZE;
        } else {
            numsOfReviews = likeStoreList.size();
        }

        Long nextCursorId = likeStoreList.get(numsOfReviews-1).getId();
        return new Paging<Long>(hasNext, numsOfReviews, nextCursorId, nextCursorId);
    }
}
