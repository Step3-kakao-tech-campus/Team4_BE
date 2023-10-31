package com.ktc.matgpt.chatgpt.dto;

import com.ktc.matgpt.feature_review.review.entity.Review;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class GptRequestConverter {

    private static final String MODEL_TYPE = "gpt-3.5-turbo";
    private static final String CODE_BLOCK = "\n'''\n";
    private static final String REVIEW_CONTENT_PREFIX = "내가 제공해주는 음식점에 대한 모든 리뷰들을 합쳐서 한 문장으로 요약해줘. " + CODE_BLOCK;
    private static final String GUIDANCE_CONTENT_PREFIX = "음식점에 다른 나라의 손님이 왔어." +
        " 그 손님이 어느 나라 사람인지 알려줄테니까, 그 나라의 음식 문화를 고려하여 음식점 사장님께 주문할 때 주문할 말을 알려줘." +
        " 예를 들어, `인도` 라는 정보를 준다면 인도는 힌두교를 믿는 사람들이 많기 때문에, 힌두교가 소고기를 먹지 않는 특성을 이용해서 다음과 같이 말할 수 있겠지."
        + "`저는 힌두교를 믿는 사람이에요. 소고기가 들어간 음식이 있다면 말씀해주세요.`"
        + "이제 사람의 나라를 알려줄게." + CODE_BLOCK;

    private final String content;

    public GptRequestConverter(String country) {
        this.content = GUIDANCE_CONTENT_PREFIX + country + CODE_BLOCK;
    }

    public GptRequestConverter(List<Review> reviews) {
        this.content = reviews.stream()
                .map(Review::getContent)
                .collect(Collectors.joining("\n", REVIEW_CONTENT_PREFIX, CODE_BLOCK));
    }

    public GptRequest convert() {
        log.info("[ChatGPT API] Request Content: " + this.content);
        return new GptRequest(MODEL_TYPE, content);
    }
}
