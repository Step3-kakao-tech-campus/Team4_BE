package com.ktc.matgpt.chatgpt.entity;

import com.ktc.matgpt.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Table(name="gpt_review_tb")
@Entity
public class GptReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false, length = 1000)
    private String content;

    private String summaryType;

    private int lastNumsOfReview;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public GptReview(String content, Long storeId, String summaryType, int lastNumsOfReview, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.content = content;
        this.storeId = storeId;
        this.summaryType = summaryType;
        this.lastNumsOfReview = lastNumsOfReview;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateContent(String content, LocalDateTime updatedAt) {
        this.content = content;
        this.updatedAt = updatedAt;
    }

    public void updateLastNumsOfReview(int numsOfReview) {
        this.lastNumsOfReview = numsOfReview;
    }
}
