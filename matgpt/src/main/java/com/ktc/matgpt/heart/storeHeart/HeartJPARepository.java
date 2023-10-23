package com.ktc.matgpt.heart.storeHeart;

import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartJPARepository extends JpaRepository<Heart, Long> {

    @Query("select h from Heart h where h.user.id = :userId")
    Optional<Heart> findHeartByUserId(Long userId);

    @Query( "select h.store from Heart h where h.user.id = :userId")
    List<Store> findStoresByUserId(Long userId);

    Optional<Heart> findHeartByUserIdAndStoreId(Long userId, Long storeId);

    boolean existsByUserAndStore(User user, Store store);

    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
    void deleteByUserAndStore(User user, Store store);


}
