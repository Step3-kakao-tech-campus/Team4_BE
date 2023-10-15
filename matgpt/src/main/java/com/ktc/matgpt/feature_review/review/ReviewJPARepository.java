package com.ktc.matgpt.feature_review.review;


import com.ktc.matgpt.feature_review.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewJPARepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.id = :reviewId")
    Optional<Review> findByReviewId(Long reviewId);

    @Query("select r from Review r where r.store.id = :storeId")
    List<Review> findAllByStoreId(Long storeId);

    @Query("select r from Review r join fetch r.store where r.userId = :userId")
    List<Review> findAllByUserId(Long userId);

    @Query(nativeQuery = true, value = "SELECT * FROM review_tb r " +
            "WHERE id < :cursorId AND store_id = :storeId " +
            "ORDER BY id DESC " +
            "LIMIT :size")
    List<Review> findAllByStoreIdAndOrderByIdDesc(Long storeId, Long cursorId, int size);

    @Query(nativeQuery = true, value = "SELECT * FROM review_tb r " +
            "WHERE store_id = :storeId AND (rating < :cursorRating OR (rating = :cursorRating AND id < :cursorId)) " +
            "ORDER BY rating DESC, id DESC " +
            "LIMIT :size")
    List<Review> findAllByStoreIdAndOrderByRatingDesc(Long storeId, Long cursorId, double cursorRating, int size);


    @Query(nativeQuery = true, value = "select * FROM review_tb r " +
            "WHERE user_id = :userId " +
            "ORDER BY id DESC " +
            "LIMIT :size " +
            "OFFSET (:pageNum-1) * :size")
    List<Review> findAllByUserIdAndOrderByIdDesc(Long userId, int pageNum, int size);

    @Query(nativeQuery = true, value = "select * FROM review_tb r " +
            "WHERE user_id = :userId " +
            "ORDER BY rating DESC, id DESC " +
            "LIMIT :size " +
            "OFFSET (:pageNum-1) * :size")
    List<Review> findAllByUserIdAndOrderByRatingDesc(Long userId, int pageNum, int size);

}
