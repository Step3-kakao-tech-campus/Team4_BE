package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeReviewJPARepository extends JpaRepository<LikeReview, Long> {
    @Query("select lr from LikeReview lr join fetch lr.user join fetch lr.review where lr.user.id = :userId")
    List<LikeReview> findAllByUserId(Long userId);

    void deleteByUserAndReview(User userRef, Review reviewRef);

    void deleteAllByReviewId(Long reviewId);

    boolean existsByUserAndReview(User userRef, Review reviewRef);
}
