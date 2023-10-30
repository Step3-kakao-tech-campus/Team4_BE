package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface LikeReviewJPARepository extends JpaRepository<LikeReview, Long> {
    List<Review> findLikedReviewsByUserId(Long id);

    void deleteByUserAndReview(User userRef, Review reviewRef);

    boolean existsByUserAndReview(User userRef, Review reviewRef);
}
