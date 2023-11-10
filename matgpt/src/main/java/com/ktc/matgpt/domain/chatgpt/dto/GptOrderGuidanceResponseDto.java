package com.ktc.matgpt.domain.chatgpt.dto;

import com.ktc.matgpt.domain.chatgpt.entity.GptOrderGuidance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GptOrderGuidanceResponseDto {

    private Long id;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public GptOrderGuidanceResponseDto(GptOrderGuidance gptOrderGuidance) {
        this.id = gptOrderGuidance.getId();
        this.content = gptOrderGuidance.getContent();
        this.createdAt = gptOrderGuidance.getCreatedAt();
    }
}
