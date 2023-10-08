package com.ktc.matgpt.feature_review.review;


import com.ktc.matgpt.feature_review.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewJPARepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.id = :reviewId")
    Optional<Review> findByReviewId(Long reviewId);


}
