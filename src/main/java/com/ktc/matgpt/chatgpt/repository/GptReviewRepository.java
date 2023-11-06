package com.ktc.matgpt.chatgpt.repository;

import com.ktc.matgpt.chatgpt.entity.GptReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GptReviewRepository extends JpaRepository<GptReview, Long> {
    List<GptReview> findByStoreId(Long storeId);
    Optional<GptReview> findByStoreIdAndSummaryType(Long storeId, String summaryType);
}
