package com.ktc.matgpt.domain.chatgpt.dto;

public record GptReviewSummaryResponseDto<T>(
        boolean isExist,
        T content
) {}
