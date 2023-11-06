package com.ktc.matgpt.chatgpt.dto;

public record GptResponseDto<T>(
        boolean isExist,
        T content
) {}
