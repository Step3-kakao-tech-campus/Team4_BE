package com.ktc.matgpt.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageJPARepository extends JpaRepository<Image, Long> {
    @Query("select i from Image i where i.review.id = :reviewId")
    List<Image> findAllByReviewId(Long reviewId);

    @Query("select i.url from Image i where i.review.id = :reviewId")
    List<String> findAllImagesByReviewId(Long reviewId);
}
