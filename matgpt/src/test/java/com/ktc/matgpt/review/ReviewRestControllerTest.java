package com.ktc.matgpt.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.review.dto.ReviewRequest;
import com.ktc.matgpt.security.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ReviewRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper ob;

    @DisplayName("개별 리뷰 상세조회")
    @Test
    public void findReviewById_test() throws Exception {
        //given
        String reviewId = "1";

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("임시 리뷰 저장")
    @Test
    public void createTemporaryReview_test() throws Exception {
        //given
        String storeId = "1";
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));
        String requestBody = ob.writeValueAsString(constructTempReviewCreateDTO());

        //when
        ResultActions resultActions = mvc.perform(
                post("/stores/"+ storeId +"/reviews/temp")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("리뷰 생성 완료")
    @Test
    public void completeReview_test() throws Exception {
        //given
        String storeId = "1";
        String reviewId = "41";

        //when
        ResultActions resultActions = mvc.perform(
                post("/stores/"+ storeId +"/complete/"+ reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("음식점 리뷰 목록(5개 단위) 조회 최신순")
    @Test
    public void findReviewsByStoreIdWithCursorPagingSortByLatest_test() throws Exception {
        //given
        String sortBy = "latest";
        String cursorId = "9";

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores/1/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("음식점 리뷰 목록(5개 단위) 조회 평점순")
    @Test
    public void findReviewsByStoreIdWithCursorPagingSortByRating_test() throws Exception{
        //given
        String sortBy = "rating";
        String cursorId = "9";
        String cursorRating = "5.0";

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores/1/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId +"&cursorRating="+ cursorRating)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("리뷰 수정")
    @Test
    public void updateReview_test() throws Exception{

        //given
        Long reviewId = 1L;
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));
        String requestBody = ob.writeValueAsString(new ReviewRequest.UpdateDTO("리뷰-1의 내용이 수정된 결과입니다."));

        //when
        ResultActions resultActions = mvc.perform(
                put("/stores/"+reviewId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }


    @DisplayName("리뷰 삭제")
    @Test
    public void deleteReview_test() throws Exception{
        //given
        Long reviewId = 1L;
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        //when
        ResultActions resultActions = mvc.perform(
                delete("/stores/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    public ReviewRequest.SimpleCreateDTO constructTempReviewCreateDTO() throws IOException {

        InputStream fi1 = new ByteArrayInputStream(new File("src/main/resources/images/image1.png"));
        InputStream fi2 = new ByteArrayInputStream(new File("src/main/resources/images/image2.png"));

        MockMultipartFile image1 = new MockMultipartFile("image1", "image1.png", "image/png", fi1);
        MockMultipartFile image2 = new MockMultipartFile("image2", "image2.png", "image/png", fi2);

        List<ReviewRequest.SimpleCreateDTO.ImageDTO> images = Arrays.asList(
                new ReviewRequest.SimpleCreateDTO.ImageDTO(image1),
                new ReviewRequest.SimpleCreateDTO.ImageDTO(image2)
        );


        return ReviewRequest.SimpleCreateDTO.builder()
                .reviewImages(images)
                .content("리뷰 임시 저장 확인 - 리뷰 내용입니다.")
                .rating(3.4)
                .peopleCount(4)
                .totalPrice(36700)
                .build();
    }

//    public ReviewRequest.CreateCompleteDTO constructReviewCompleteDTO() {
//
//    }
}
