package com.ktc.matgpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.domain.review.dto.ReviewRequest;

import java.util.Arrays;
import java.util.List;

public class TestHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // SimpleCreateDTO 객체를 생성하는 메서드
    public static String constructTempReviewCreateDTO() throws Exception {
        String content = "이것은 테스트 리뷰입니다.";
        int rating = 5;
        int peopleCount = 2;
        int totalPrice = 50000;
        int imageCount = 5;

        return objectMapper.writeValueAsString(new ReviewRequest.SimpleCreateDTO(content, rating, peopleCount, totalPrice, imageCount));
    }

    // 기존 테스트 클래스 내에 추가
    public static String constructCompleteDTO() throws Exception {
        // 태그 정보 생성
        ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tag1 = new ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO("짜장면", 50, 100, 3);
        ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tag2 = new ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO("짬뽕", 150, 200, 1);
        ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tag3 = new ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO("탕수육", 250, 300, 4);

        // 이미지 및 태그 정보를 설정
        ReviewRequest.CreateCompleteDTO.ImageDTO imageDTO = new ReviewRequest.CreateCompleteDTO.ImageDTO("https://example.com/image1.jpg", Arrays.asList(tag1, tag2, tag3));

        List<ReviewRequest.CreateCompleteDTO.ImageDTO> reviewImages = Arrays.asList(imageDTO);
        return objectMapper.writeValueAsString(new ReviewRequest.CreateCompleteDTO(reviewImages));

    }
}
