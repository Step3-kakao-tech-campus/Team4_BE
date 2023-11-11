package com.ktc.matgpt.domain.chatgpt.entity;

import com.ktc.matgpt.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Table(name="gpt_guidance_tb")
@Entity
public class GptOrderGuidance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=1000, nullable=false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public GptOrderGuidance(User user, String content, LocalDateTime createdAt) {
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static GptOrderGuidance create(User user, String content, LocalDateTime createdAt) {
        return new GptOrderGuidance(user, content, createdAt);
    }
}
