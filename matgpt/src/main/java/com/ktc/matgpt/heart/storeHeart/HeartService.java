package com.ktc.matgpt.heart.storeHeart;


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
public class HeartService {

    private final StoreService storeService;
    private final UserService userService;
    private final HeartJPARepository heartJPARepository;

    private final EntityManager entityManager;

    @Transactional
    public void addHeartToStore(Long storeId, String email){

        User userRef = userService.getReferenceByEmail(email);
        Store storeRef = storeService.getReferenceById(storeId);

        if (isHeartAlreadyExists(userRef.getId(), storeId)) {
            throw new IllegalArgumentException("이미 하트가 눌러진 상태입니다.");
        }

        //없을 경우에 repo에 저장
        Heart heart = Heart.builder().user(userRef).store(storeRef).build();

        heartJPARepository.save(heart);
    }

    @Transactional
    public void deleteHeartToStore(Long storeId, String email) {

        User user = userService.findByEmail(email);
        Store store = storeService.findById(storeId);

        if (!isHeartAlreadyExists(user.getId(), storeId)) {
            throw new IllegalArgumentException("좋아요가 없습니다.");
        }

        //좋아요가 있을 경우 하트 삭제
        heartJPARepository.deleteByUserAndStore(user, store);
    }

    public HeartResponseDTO.FindAllLikeStoresDTO findStoresByUserEmail(String email) {
        User user = userService.findByEmail(email);
        List<Store> stores=  heartJPARepository.findStoresByUserId(user.getId()).stream().toList();
        return new HeartResponseDTO.FindAllLikeStoresDTO(user,stores);
    }

    private boolean isHeartAlreadyExists(Long userId, Long storeId) {
        return heartJPARepository.existsByUserIdAndStoreId(userId, storeId);
    }

}
