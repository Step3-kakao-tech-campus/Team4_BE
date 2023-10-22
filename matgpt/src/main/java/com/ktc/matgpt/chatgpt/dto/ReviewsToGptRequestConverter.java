package com.ktc.matgpt.chatgpt.dto;

import com.ktc.matgpt.chatgpt.factory.MockReview;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ReviewsToGptRequestConverter {

    private static final String CODE_BLOCK = "\n'''\n";
    private static final String CONTENT_PREFIX = "내가 제공해주는 음식점에 대한 모든 리뷰들을 합쳐서 한 문장으로 요약해줘. " + CODE_BLOCK;
    private static final String MODEL_TYPE = "gpt-3.5-turbo";

    public static GptRequest convert(List<MockReview> mockReviews) {
        String content = mockReviews.stream()
                        .map(MockReview::content)
                        .collect(Collectors.joining("\n", CONTENT_PREFIX, CODE_BLOCK));

        log.info("[ChatGPT API] Request Content: " + content);

        return new GptRequest(MODEL_TYPE, content);
    }
}
