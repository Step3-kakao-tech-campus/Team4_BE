package com.ktc.matgpt.review;


import com.ktc.matgpt.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewJPARepository extends JpaRepository<Review, Long> {
    List<Review> findByStoreId(Long storeId, Pageable pageable);

    // 음식점 리뷰 목록 조회 - 최신순, 커서 기반
    @Query("SELECT r FROM Review r " +
            "WHERE r.store.id = :storeId AND r.id < :cursorId " +
            "ORDER BY r.id DESC")
    Page<Review> findAllByStoreIdAndOrderByIdDesc(Long storeId, Long cursorId, Pageable page);

    // 음식점 리뷰 목록 조회 - 추천순, 커서 기반
    @Query("SELECT r FROM Review r " +
            "WHERE r.store.id = :storeId AND (r.recommendCount < :cursorLikes OR (r.recommendCount= :cursorLikes AND r.id < :cursorId)) " +
            "ORDER BY r.recommendCount DESC, r.id DESC")
    Page<Review> findAllByStoreIdAndOrderByLikesAndIdDesc(Long storeId, Long cursorId, Integer cursorLikes, Pageable page);


    // 마이페이지 작성한 리뷰 조회 - 최신순, 커서 기반
    @Query("SELECT r FROM Review r " +
            "WHERE r.user.id = :userId AND r.id < :cursorId " +
            "ORDER BY r.id DESC")
    Page<Review> findAllByUserIdAndOrderByIdDesc(Long userId, Long cursorId, Pageable page);

    // 마이페이지 작성한 리뷰 조회 - 추천순, 커서 기반
    @Query("SELECT r FROM Review r " +
            "WHERE r.user.id = :userId AND (r.recommendCount < :cursorLikes OR (r.recommendCount= :cursorLikes AND r.id < :cursorId)) " +
            "ORDER BY r.recommendCount DESC, r.id DESC")
    Page<Review> findAllByUserIdAndOrderByLikesAndIdDesc(Long userId, Long cursorId, Integer cursorLikes, Pageable page);

    // 모든 리뷰 조회 - 최신순, 커서 기반
    @Query("SELECT r FROM Review r " +
            "WHERE r.createdAt < :cursor OR (r.createdAt = :cursor AND r.id < :cursorId)" +
            "ORDER BY r.createdAt DESC, r.id DESC")
    List<Review> findAllLessThanCursorOrderByCreatedAtDesc(Long cursorId, LocalDateTime cursor, Pageable page);
}
