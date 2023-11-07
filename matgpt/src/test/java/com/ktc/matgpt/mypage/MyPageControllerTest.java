package com.ktc.matgpt.mypage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.security.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "classpath:custom_modified.sql")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class MyPageControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper ob;

    @DisplayName("마이페이지 작성한 리뷰 목록(8개 단위) 조회 최신순")
    @Test
    public void findReviewsByUserIdSortByLatest_test() throws Exception{
        //given
        // userId 1이 작성한 리뷰는 총 10개
        String sortBy = "latest";
        String cursorId = "";
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        //when
        ResultActions resultActions = mvc.perform(
                get("/mypage/my-reviews?sortBy="+ sortBy +"&cursorId="+ cursorId)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        // 리뷰 데이터 응답 검증
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.body[0].id").value("19"));
        resultActions.andExpect(jsonPath("$.data.body[1].id").value("18"));
        resultActions.andExpect(jsonPath("$.data.body[2].id").value("17"));
        resultActions.andExpect(jsonPath("$.data.body[3].id").value("16"));
        resultActions.andExpect(jsonPath("$.data.body[4].id").value("15"));
        resultActions.andExpect(jsonPath("$.data.body[5].id").value("14"));
        resultActions.andExpect(jsonPath("$.data.body[6].id").value("13"));
        resultActions.andExpect(jsonPath("$.data.body[7].id").value("12"));
        // 페이징 관련 데이터 응답 검증
        resultActions.andExpect(jsonPath("$.data.paging.hasNext").value(true));
        resultActions.andExpect(jsonPath("$.data.paging.countOfReviews").value(8));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursorId").value(12));

        //given
        cursorId = "12";

        //when - 2차 요청 (cursor는 이전 요청의 마지막 리뷰 id)
        resultActions = mvc.perform(
                get("/mypage/my-reviews?sortBy="+ sortBy +"&cursorId="+ cursorId)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // verify
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.body[0].id").value("11"));
        resultActions.andExpect(jsonPath("$.data.body[1].id").value("1"));
        // 페이징 관련 데이터 응답 검증
        resultActions.andExpect(jsonPath("$.data.paging.hasNext").value(false));
        resultActions.andExpect(jsonPath("$.data.paging.countOfReviews").value(2));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursorId").value(1));
    }

    @DisplayName("마이페이지 작성한 리뷰 목록(8개 단위) 조회 평점순")
    @Test
    public void findReviewsByUserIdSortByRating_test() throws Exception{
        //given
        String sortBy = "likes";
        String cursorId = "";
        String cursorLikes = "";
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        //when
        ResultActions resultActions = mvc.perform(
                get("/mypage/my-reviews?sortBy="+ sortBy +"&cursorId="+ cursorId +"&cursorLikes="+ cursorLikes)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        // 리뷰 데이터 응답 검증
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.body[0].id").value("12"));
        resultActions.andExpect(jsonPath("$.data.body[0].numOfLikes").value("5"));
        resultActions.andExpect(jsonPath("$.data.body[1].id").value("11"));
        resultActions.andExpect(jsonPath("$.data.body[1].numOfLikes").value("5"));
        resultActions.andExpect(jsonPath("$.data.body[2].id").value("13"));
        resultActions.andExpect(jsonPath("$.data.body[2].numOfLikes").value("4"));
        resultActions.andExpect(jsonPath("$.data.body[3].id").value("15"));
        resultActions.andExpect(jsonPath("$.data.body[3].numOfLikes").value("3"));
        resultActions.andExpect(jsonPath("$.data.body[4].id").value("1"));
        resultActions.andExpect(jsonPath("$.data.body[4].numOfLikes").value("2"));
        resultActions.andExpect(jsonPath("$.data.body[5].id").value("18"));
        resultActions.andExpect(jsonPath("$.data.body[5].numOfLikes").value("1"));
        resultActions.andExpect(jsonPath("$.data.body[6].id").value("17"));
        resultActions.andExpect(jsonPath("$.data.body[6].numOfLikes").value("1"));
        resultActions.andExpect(jsonPath("$.data.body[7].id").value("19"));
        resultActions.andExpect(jsonPath("$.data.body[7].numOfLikes").value("0"));
        // 페이징 관련 데이터 응답 검증
        resultActions.andExpect(jsonPath("$.data.paging.hasNext").value(true));
        resultActions.andExpect(jsonPath("$.data.paging.countOfReviews").value(8));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursorId").value(19));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursorLikes").value(0));

        //given
        cursorId = "19";
        cursorLikes = "0";

        //when - 2차 요청 (cursor는 이전 요청의 마지막 리뷰 id, numOfLikes)
        resultActions = mvc.perform(
                get("/mypage/my-reviews?sortBy="+ sortBy +"&cursorId="+ cursorId +"&cursorLikes="+ cursorLikes)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        // 리뷰 데이터 응답 검증
        resultActions.andExpect(jsonPath("$.data.body[0].id").value("16"));
        resultActions.andExpect(jsonPath("$.data.body[0].numOfLikes").value("0"));
        resultActions.andExpect(jsonPath("$.data.body[1].id").value(14));
        resultActions.andExpect(jsonPath("$.data.body[1].numOfLikes").value(0));
        // 페이징 관련 데이터 응답 검증
        resultActions.andExpect(jsonPath("$.data.paging.hasNext").value(false));
        resultActions.andExpect(jsonPath("$.data.paging.countOfReviews").value(2));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursorId").value(14));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursorLikes").value(0));
    }

    @DisplayName("마이페이지 좋아요한 리뷰 목록(8개 단위) 조회 최신순")
    @Test
    public void findLikeReviewsByUserIdSortByLatest_test() throws Exception{
        //given
        // userId 1이 좋아요 누른 리뷰: 총 4개(reviewId: 1, 2, 3, 7)
        // 좋아요 누른 순서: 1 -> 2 -> 7 -> 3
        String cursorId = "";
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        //when
        ResultActions resultActions = mvc.perform(
                get("/mypage/liked-reviews?cursorId="+ cursorId)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        // 리뷰 데이터 응답 검증 - 리뷰 최신순 x, 리뷰에 좋아요를 누른 최신순으로 정렬된다.
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.body[0].id").value("3"));
        resultActions.andExpect(jsonPath("$.data.body[1].id").value("7"));
        resultActions.andExpect(jsonPath("$.data.body[2].id").value("2"));
        resultActions.andExpect(jsonPath("$.data.body[3].id").value("1"));
        // 페이징 관련 데이터 응답 검증
        resultActions.andExpect(jsonPath("$.data.paging.hasNext").value(false));
        resultActions.andExpect(jsonPath("$.data.paging.countOfReviews").value(4));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursorId").value(1));
    }
}
