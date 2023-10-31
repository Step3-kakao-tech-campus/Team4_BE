package com.ktc.matgpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.review.dto.ReviewRequest;

public class TestHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // SimpleCreateDTO 객체를 생성하는 메서드
    public static ReviewRequest.SimpleCreateDTO constructTempReviewCreateDTO() {
        String content = "이것은 테스트 리뷰입니다.";
        double rating = 4.5;
        int peopleCount = 2;
        int totalPrice = 50000;

        return new ReviewRequest.SimpleCreateDTO(content, rating, peopleCount, totalPrice);
    }

    // SimpleCreateDTO 객체를 JSON 문자열로 변환하는 메서드
    public static String convertToJSON(ReviewRequest.SimpleCreateDTO tempReviewCreateDTO) throws Exception {
        return objectMapper.writeValueAsString(tempReviewCreateDTO);
    }
}
