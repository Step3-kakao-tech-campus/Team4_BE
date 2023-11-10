package com.ktc.matgpt.likeReview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.security.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "classpath:custom_modified.sql")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class LikeReviewRestControllerTest {
    @Autowired
    private MockMvc mvc;


    @DisplayName("리뷰 좋아요 토글 테스트 - 좋아요 등록")
    @Test
    public void testToggleLike_Add() throws Exception {
        Long userId = 1L;
        String userEmail = "nstgic3@gmail.com";
        Long reviewId = 10L;

        UserPrincipal userPrincipal = new UserPrincipal(userId, userEmail, false, Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST")));

        mvc.perform(post("/reviews/" + reviewId + "/like")
                        .with(oauth2Login().oauth2User(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("리뷰 좋아요 등록"));

    }

    @DisplayName("리뷰 좋아요 토글 테스트 - 좋아요 취소")
    @Test
    public void testToggleLike_Delete() throws Exception {
        Long userId = 1L;
        String userEmail = "nstgic3@gmail.com";
        Long reviewId = 1L;

        UserPrincipal userPrincipal = new UserPrincipal(userId, userEmail, false, Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST")));

        mvc.perform(post("/reviews/" + reviewId + "/like")
                        .with(oauth2Login().oauth2User(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("리뷰 좋아요 취소"));
    }

    @DisplayName("리뷰 좋아요 여부 확인 테스트 - 참")
    @Test
    public void testCheckIfAlreadyLiked_true() throws Exception {
        Long reviewId = 1L;
        Long userId = 1L;
        String mockEmail = "nstgic3@gmail.com";

        UserPrincipal userPrincipal = new UserPrincipal(userId, mockEmail, false, Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST")));

        mvc.perform(get("/reviews/" + reviewId + "/liked")
                        .with(oauth2Login().oauth2User(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.hasLiked").value(true));
    }

    @DisplayName("리뷰 좋아요 여부 확인 테스트 - 거짓")
    @Test
    public void testCheckIfAlreadyLiked_false() throws Exception {
        Long reviewId = 10L;
        Long userId = 1L;
        String mockEmail = "nstgic3@gmail.com";

        UserPrincipal userPrincipal = new UserPrincipal(userId, mockEmail, false, Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST")));

        mvc.perform(get("/reviews/" + reviewId + "/liked")
                        .with(oauth2Login().oauth2User(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.hasLiked").value(false));
    }
}
