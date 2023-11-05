package com.ktc.matgpt.chatgpt.dto;

import java.util.concurrent.CompletableFuture;

public record GptResponse (
        Long storeId,
        String summaryType,
        CompletableFuture<GptApiResponse> gptApiResponse
) {}
