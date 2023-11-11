package com.ktc.matgpt.domain.chatgpt.dto;

import java.util.concurrent.CompletableFuture;

public record GptReviewSummaryResponse(
        Long storeId,
        String summaryType,
        CompletableFuture<GptApiResponse> gptApiResponse
) {}
