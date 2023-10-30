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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class MyPageControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper ob;

    @DisplayName("마이페이지 작성한 리뷰 목록(5개 단위) 조회 최신순")
    @Test
    public void findReviewsByUserIdWithOffsetPagingSortByLatest_test() throws Exception{
        //given
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));
        String sortBy = "latest";
        String pageNum = "1";

        //when
        ResultActions resultActions = mvc.perform(
                get("/mypage/my-reviews?sortBy="+ sortBy +"&pageNum="+ pageNum)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("마이페이지 작성한 리뷰 목록(5개 단위) 조회 평점순")
    @Test
    public void findReviewsByUserIdWithOffsetPagingSortByRating_test() throws Exception{
        //given
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));
        String sortBy = "rating";
        String pageNum = "1";

        //when
        ResultActions resultActions = mvc.perform(
                get("/mypage/my-reviews?sortBy="+ sortBy +"&pageNum="+ pageNum)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }
}
