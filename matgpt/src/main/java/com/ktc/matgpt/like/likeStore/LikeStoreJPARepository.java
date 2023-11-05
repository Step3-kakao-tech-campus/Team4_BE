package com.ktc.matgpt.like.likeStore;

import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeStoreJPARepository extends JpaRepository<LikeStore, Long> {

    @Query("select h from LikeStore h where h.user.id = :userId")
    Optional<LikeStore> findHeartByUserId(Long userId);

    @Query( "select h.store from LikeStore h where h.user.id = :userId")
    List<Store> findLikedStoresByUserId(Long userId);
    boolean existsByUserAndStore(User userRef, Store storeRef);
    void deleteByUserAndStore(User user, Store store);


}
