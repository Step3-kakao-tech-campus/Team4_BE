package com.ktc.matgpt.chatgpt.entity;

import com.ktc.matgpt.store.Store;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @Column(nullable = false, length = 1000)
    private String content;

    private String summaryType;

    private int lastNumsOfReview;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public GptReview(String content, Store store, String summaryType, int lastNumsOfReview, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.content = content;
        this.store = store;
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
