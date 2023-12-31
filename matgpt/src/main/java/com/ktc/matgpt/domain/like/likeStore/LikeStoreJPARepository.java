package com.ktc.matgpt.domain.like.likeStore;

import com.ktc.matgpt.domain.store.Store;
import com.ktc.matgpt.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeStoreJPARepository extends JpaRepository<LikeStore, Long> {

    @Query("select h from LikeStore h " +
            "where h.user.id = :userId")
    Optional<LikeStore> findHeartByUserId(Long userId);

    @Query( "select h.store from LikeStore h " +
            "where h.user.id = :userId")
    List<Store> findLikedStoresByUserId(Long userId);

    @Query( "select h from LikeStore h " +
            "join fetch h.user " +
            "join fetch h.store " +
            "where h.user.id = :userId and h.id < :cursorId " +
            "order by h.id desc")
    List<LikeStore> findLikedStoresByUserId(Long userId, Long cursorId, Pageable page);
    boolean existsByUserAndStore(User userRef, Store storeRef);
    void deleteByUserAndStore(User user, Store store);


}
