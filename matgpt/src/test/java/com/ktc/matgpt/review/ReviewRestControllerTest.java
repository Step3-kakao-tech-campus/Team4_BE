package com.ktc.matgpt.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.review.dto.ReviewRequest;
import com.ktc.matgpt.security.UserPrincipal;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.web.multipart.MultipartFile;

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
        String storeId = "1";

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @Disabled
    @DisplayName("임시 리뷰 저장")
    @Test
    public void createTemporaryReview_test() throws Exception {
        //given
        String storeId = "1";
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));
        String requestBody = ob.writeValueAsString(null /*constructTempReviewCreateDTO()*/);

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

    @Disabled
    @DisplayName("리뷰 생성 완료")
    @Test
    public void completeReview_test() throws Exception {
        //given
        String storeId = "1";
        String reviewId = "41";

        //when
        ResultActions resultActions = mvc.perform(
                post("/stores/"+ storeId +"/reviews/complete/"+ reviewId)
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
        String cursorId = "41";
        String storeId = "1";

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.response[0].reviewId").value("16"));
        resultActions.andExpect(jsonPath("$.response[1].reviewId").value("15"));
        resultActions.andExpect(jsonPath("$.response[2].reviewId").value("14"));
        resultActions.andExpect(jsonPath("$.response[3].reviewId").value("13"));
        resultActions.andExpect(jsonPath("$.response[4].reviewId").value("12"));

        //given
        cursorId = "12";
        //when
        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.response[0].reviewId").value("11"));
        resultActions.andExpect(jsonPath("$.response[1].reviewId").value("10"));
        resultActions.andExpect(jsonPath("$.response[2].reviewId").value("9"));
        resultActions.andExpect(jsonPath("$.response[3].reviewId").value("8"));
        resultActions.andExpect(jsonPath("$.response[4].reviewId").value("7"));

        //given
        cursorId = "7";
        //when
        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.response[0].reviewId").value("6"));
        resultActions.andExpect(jsonPath("$.response[1].reviewId").value("5"));
        resultActions.andExpect(jsonPath("$.response[2].reviewId").value("4"));
        resultActions.andExpect(jsonPath("$.response[3].reviewId").value("3"));
        resultActions.andExpect(jsonPath("$.response[4].reviewId").value("2"));

        //given
        cursorId = "2";
        //when
        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.response[0].reviewId").value("1"));
    }

    @DisplayName("음식점 리뷰 목록(5개 단위) 조회 평점순")
    @Test
    public void findReviewsByStoreIdWithCursorPagingSortByRating_test() throws Exception{
        //given
        String storeId = "1";
        String sortBy = "rating";
        String cursorId = "41";
        String cursorRating = "5.0";

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId +"&cursorRating="+ cursorRating)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.response[0].reviewId").value("11"));
        resultActions.andExpect(jsonPath("$.response[0].rating").value("5.0"));
        resultActions.andExpect(jsonPath("$.response[1].reviewId").value("10"));
        resultActions.andExpect(jsonPath("$.response[1].rating").value("5.0"));
        resultActions.andExpect(jsonPath("$.response[2].reviewId").value("9"));
        resultActions.andExpect(jsonPath("$.response[2].rating").value("5.0"));
        resultActions.andExpect(jsonPath("$.response[3].reviewId").value("8"));
        resultActions.andExpect(jsonPath("$.response[3].rating").value("5.0"));
        resultActions.andExpect(jsonPath("$.response[4].reviewId").value("7"));
        resultActions.andExpect(jsonPath("$.response[4].rating").value("5.0"));

        //given
        cursorId = "7";
        cursorRating = "5.0";

        //when
        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId +"&cursorRating="+ cursorRating)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //console
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        // verify
        resultActions.andExpect(jsonPath("$.response[0].reviewId").value("6"));
        resultActions.andExpect(jsonPath("$.response[0].rating").value("5.0"));
        resultActions.andExpect(jsonPath("$.response[1].reviewId").value("5"));
        resultActions.andExpect(jsonPath("$.response[1].rating").value("5.0"));
        resultActions.andExpect(jsonPath("$.response[2].reviewId").value("4"));
        resultActions.andExpect(jsonPath("$.response[2].rating").value("5.0"));
        resultActions.andExpect(jsonPath("$.response[3].reviewId").value("3"));
        resultActions.andExpect(jsonPath("$.response[3].rating").value("5.0"));
        resultActions.andExpect(jsonPath("$.response[4].reviewId").value("1"));
        resultActions.andExpect(jsonPath("$.response[4].rating").value("5.0"));

        //given
        cursorId = "1";
        cursorRating = "5.0";

        //when
        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId +"&cursorRating="+ cursorRating)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //console
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        // verify
        resultActions.andExpect(jsonPath("$.response[0].reviewId").value("12"));
        resultActions.andExpect(jsonPath("$.response[0].rating").value("4.0"));
        resultActions.andExpect(jsonPath("$.response[1].reviewId").value("2"));
        resultActions.andExpect(jsonPath("$.response[1].rating").value("4.0"));
        resultActions.andExpect(jsonPath("$.response[2].reviewId").value("13"));
        resultActions.andExpect(jsonPath("$.response[2].rating").value("3.0"));
        resultActions.andExpect(jsonPath("$.response[3].reviewId").value("16"));
        resultActions.andExpect(jsonPath("$.response[3].rating").value("2.0"));
        resultActions.andExpect(jsonPath("$.response[4].reviewId").value("14"));
        resultActions.andExpect(jsonPath("$.response[4].rating").value("2.0"));

        //given
        cursorId = "14";
        cursorRating = "2.0";

        //when
        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId +"&cursorRating="+ cursorRating)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //console
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        // verify
        resultActions.andExpect(jsonPath("$.response[0].reviewId").value("15"));
        resultActions.andExpect(jsonPath("$.response[0].rating").value("1.0"));
    }

    @DisplayName("리뷰 수정")
    @Test
    public void updateReview_test() throws Exception{

        //given
        String storeId = "1";
        String reviewId = "1";
        String content = "리뷰-1의 내용이 수정된 결과입니다.";
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));
        String requestBody = ob.writeValueAsString(new ReviewRequest.UpdateDTO(content));

        //when
        ResultActions resultActions = mvc.perform(
                put("/stores/"+ storeId +"/reviews/"+reviewId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //console
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.response.content").value(content));
    }


    @DisplayName("리뷰 삭제")
    @Test
    public void deleteReview_test() throws Exception{
        //given
        String storeId = "1";
        String reviewId = "1";

        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));
        String notFoundMessage = "[MatGPT] 요청한 리뷰를 찾을 수 없습니다.";

        //when - 리뷰 삭제
        ResultActions resultActions = mvc.perform(
                delete("/stores/"+ storeId +"/reviews/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );
        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));

        //when - 리뷰 삭제 후 조회
        resultActions = mvc.perform(
                delete("/stores/"+ storeId +"/reviews/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );
        //console
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        // verify
        resultActions.andExpect(jsonPath("$.errorCode").value(404));
        resultActions.andExpect(jsonPath("$.message").value(notFoundMessage));
    }
}
