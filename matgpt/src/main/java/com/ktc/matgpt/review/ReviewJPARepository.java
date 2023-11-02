package com.ktc.matgpt.review;


import com.ktc.matgpt.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewJPARepository extends JpaRepository<Review, Long> {
    List<Review> findByStoreId(Long storeId, Pageable pageable);

    @Query(nativeQuery = true, value =
            "SELECT * FROM review_tb r " +
            "WHERE store_id = :storeId AND id < :cursorId " +
            "ORDER BY id DESC " +
            "LIMIT :size")
    List<Review> findAllByStoreIdAndOrderByIdDesc(Long storeId, Long cursorId, int size);

    @Query(nativeQuery = true, value =
            "SELECT * FROM review_tb r " +
            "WHERE store_id = :storeId AND (recommend_count < :cursorLikes OR (recommend_count= :cursorLikes AND id < :cursorId)) " +
            "ORDER BY recommend_count DESC, id DESC " +
            "LIMIT :size")
    List<Review> findAllByStoreIdAndOrderByLikesDesc(Long storeId, Long cursorId, int cursorLikes, int size);


    @Query("select r FROM Review r JOIN FETCH r.store WHERE r.userId = :userId ORDER BY r.id DESC")
    Page<Review> findAllByUserIdAndOrderByIdDesc(Long userId, Pageable page);

    @Query("SELECT r FROM Review r JOIN FETCH r.store WHERE r.userId = :userId ORDER BY r.recommendCount DESC, r.id DESC")
    Page<Review> findAllByUserIdAndOrderByLikesDesc(Long userId, Pageable page);

}
