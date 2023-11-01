package com.ktc.matgpt.review;


import com.ktc.matgpt.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
            "WHERE store_id = :storeId AND (rating < :cursorRating OR (rating = :cursorRating AND id < :cursorId)) " +
            "ORDER BY rating DESC, id DESC " +
            "LIMIT :size")
    List<Review> findAllByStoreIdAndOrderByRatingDesc(Long storeId, Long cursorId, double cursorRating, int size);


    @Query("select r FROM Review r JOIN FETCH r.store WHERE r.userId = :userId ORDER BY r.id DESC")
    Page<Review> findAllByUserIdAndOrderByIdDesc(Long userId, Pageable page);

    @Query("SELECT r FROM Review r JOIN FETCH r.store WHERE r.userId = :userId ORDER BY r.rating DESC, r.id DESC")
    Page<Review> findAllByUserIdAndOrderByRatingDesc(Long userId, Pageable page);

}
