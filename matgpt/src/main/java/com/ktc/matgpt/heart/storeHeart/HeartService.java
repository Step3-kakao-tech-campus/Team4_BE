package com.ktc.matgpt.heart.storeHeart;


import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreJPARepository;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final StoreJPARepository storeJPARepository;
    private final UserRepository userRepository;
    private final HeartJPARepository heartJPARepository;


    @Transactional
    public void addHeartToStore(Long storeId, String email){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        Store store = storeJPARepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        //이미 좋아요를 눌렀을 경우
        if (heartJPARepository.findStoresByUserId(user.getId()).contains(store)){
            throw new IllegalArgumentException("이미 하트가 눌러진 상태입니다.");
        }

        //없을 경우에 repo에 저장
        Heart heart = Heart.builder().user(user).store(store).build();
        heartJPARepository.save(heart);
    }

    @Transactional
    public void deleteHeartToStore(Long storeId, String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        Store store = storeJPARepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        //좋아요가 없을 경우 error
        Heart heart = heartJPARepository.findHeartByUserId(user.getId()).orElseThrow(()-> new IllegalArgumentException("좋아요가 없습니다."));
        //좋아요가 있을 경우 하트 삭제
        heartJPARepository.delete(heart);
    }


    public HeartResponseDTO.FindAllLikeStoresDTO findStoresByUser(String email) {
        User user= userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        List<Store> stores=  heartJPARepository.findStoresByUserId(user.getId()).stream().toList();
        return new HeartResponseDTO.FindAllLikeStoresDTO(user,stores);
    }
}
