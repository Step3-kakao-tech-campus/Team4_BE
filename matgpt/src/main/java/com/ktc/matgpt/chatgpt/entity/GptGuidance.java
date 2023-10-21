package com.ktc.matgpt.chatgpt.entity;

import com.ktc.matgpt.feature_review.review.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Table(name="gpt_guidance_tb")
@Entity
public class GptGuidance extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=1000, nullable=false)
    private String content;

    private Long userId;

    @Builder
    public GptGuidance(String content, Long userId) {
        this.content = content;
        this.userId = userId;
    }
}
