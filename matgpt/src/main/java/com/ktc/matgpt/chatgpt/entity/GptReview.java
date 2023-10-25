package com.ktc.matgpt.chatgpt.entity;

import com.ktc.matgpt.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Table(name="gpt_review_tb")
@Entity
public class GptReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long storeId;

    @Column(nullable = false, length = 1000)
    private String content;

    private String summaryType;

    private int lastNumsOfReview;

    @Builder
    public GptReview(String content, Long storeId, String summaryType, int lastNumsOfReview) {
        this.content = content;
        this.storeId = storeId;
        this.summaryType = summaryType;
        this.lastNumsOfReview = lastNumsOfReview;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateLastNumsOfReview(int numsOfReview) {
        this.lastNumsOfReview = numsOfReview;
    }
}
