package com.ktc.matgpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.review.dto.ReviewRequest;

import java.util.Arrays;
import java.util.List;

public class TestHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // SimpleCreateDTO 객체를 생성하는 메서드
    public static String constructTempReviewCreateDTO() throws Exception {
        String content = "이것은 테스트 리뷰입니다.";
        double rating = 4.5;
        int peopleCount = 2;
        int totalPrice = 50000;

        return objectMapper.writeValueAsString(new ReviewRequest.SimpleCreateDTO(content, rating, peopleCount, totalPrice));
    }

    // 기존 테스트 클래스 내에 추가
    public static String constructCompleteDTO() throws Exception {
        // 태그 정보 생성
        ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tag1 = new ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO("짜장면", 50, 100, 4.5);
        ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tag2 = new ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO("짬뽕", 150, 200, 4.2);
        ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tag3 = new ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO("탕수육", 250, 300, 4.8);

        // 이미지 및 태그 정보를 설정
        ReviewRequest.CreateCompleteDTO.ImageDTO imageDTO = new ReviewRequest.CreateCompleteDTO.ImageDTO("https://example.com/image1.jpg", Arrays.asList(tag1, tag2, tag3));

        List<ReviewRequest.CreateCompleteDTO.ImageDTO> reviewImages = Arrays.asList(imageDTO);

        String content = "테스트 리뷰 내용";
        double rating = 4.5;
        int peopleCount = 2;
        int totalPrice = 50000;

        return objectMapper.writeValueAsString(new ReviewRequest.CreateCompleteDTO(reviewImages, content, rating, peopleCount, totalPrice));
    }
}
