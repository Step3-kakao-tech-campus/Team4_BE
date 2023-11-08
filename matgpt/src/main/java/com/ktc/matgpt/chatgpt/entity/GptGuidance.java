package com.ktc.matgpt.chatgpt.entity;

import com.ktc.matgpt.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Table(name="gpt_guidance_tb")
@Entity
public class GptGuidance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=1000, nullable=false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public GptGuidance(User user, String content, LocalDateTime createdAt) {
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static GptGuidance create(User user, String content, LocalDateTime createdAt) {
        return new GptGuidance(user, content, createdAt);
    }
}
