package com.ktc.matgpt.chatgpt.dto;

import com.ktc.matgpt.review.entity.Review;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Slf4j
public class GptRequestConverter {

    public static GptApiRequest convertFromCountry(String country) {
        String content = "음식점에 다른 나라의 손님이 왔어." +
                " 그 손님이 어느 나라 사람인지 알려줄테니까, 그 나라의 음식 문화를 고려하여 음식점 사장님께 주문할 때 주문할 말을 알려줘." +
                " 예를 들어, `인도` 라는 정보를 준다면 인도는 힌두교를 믿는 사람들이 많기 때문에, 힌두교가 소고기를 먹지 않는 특성을 이용해서 다음과 같이 말할 수 있겠지."
                + "`저는 힌두교를 믿는 사람이에요. 소고기가 들어간 음식이 있다면 말씀해주세요.`"
                + "이제 사람의 나라를 알려줄게.\n```\n"
                + country + "\n```\n";

        log.info("[ChatGPT API] Request Content: " + content);
        return new GptApiRequest(content);
    }

    public static GptApiRequest convertFromReviews(List<Review> reviews, String summaryType) {
        String content = "내가 제공해주는 음식점에 대한 모든 리뷰들을 합쳐서 50글자 이내로 한 문장으로 요약하고, '모든 리뷰들을 종합하면' 같은 문장은 생략하고, 요약한 문장만 말해줘. 반드시 문장을 완성해서 말해줘야 해.";
        content += switch (summaryType) {
            case "BEST" -> "긍정적인 리뷰를 강조해서 말해줘.\n```\n";
            case "WORST" -> "부정적인 리뷰를 강조해서 말해줘.\n```\n";
            default -> "\n```\n";
        };

        content = reviews.stream()
                .map(Review::getContent)
                .collect(Collectors.joining("\n", content, "\n```\n"));

        log.info("[ChatGPT API] Request Content: " + content);
        return new GptApiRequest(content);
    }
}
