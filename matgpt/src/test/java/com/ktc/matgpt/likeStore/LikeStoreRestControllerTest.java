package com.ktc.matgpt.likeStore;


import com.ktc.matgpt.like.likeStore.LikeStoreService;
import com.ktc.matgpt.security.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class LikeStoreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeStoreService likeStoreService;

    @DisplayName("즐겨찾기한 음식점 모두 불러오기")
    @Test
    public void testFindAllStores() throws Exception {
        Long mockUserId = 1L;
        String mockEmail = "nstgic3@gmail.com";

        UserPrincipal userPrincipal = new UserPrincipal(mockUserId, mockEmail, Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST")));

        mockMvc.perform(get("/stores/like")
                        .with(oauth2Login().oauth2User(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("음식점 즐겨찾기 추가 테스트v1")
    @Test
    public void testAddHeartToStore() throws Exception {
        Long mockUserId = 1L;
        String mockUsername = "nstgic3@gmail.com";
        Long mockStoreId = 1L;  // Assuming a store id for the test

        UserPrincipal userPrincipal = new UserPrincipal(mockUserId, mockUsername, Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST")));

        mockMvc.perform(post("/stores/" + mockStoreId + "/like")
                        .with(oauth2Login().oauth2User(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("음식점 즐겨찾기 추가 토글 테스트 - 즐겨찾기 추가")
    @Test
    public void testToggleHeart_Add() throws Exception {
        Long mockStoreId = 1L;
        String mockEmail = "nstgic3@gmail.com";

        when(likeStoreService.toggleHeartForStore(mockStoreId, mockEmail)).thenReturn(true);

        UserPrincipal userPrincipal = new UserPrincipal(null, mockEmail, Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST")));

        mockMvc.perform(post("/stores/" + mockStoreId + "/like")
                        .with(oauth2Login().oauth2User(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("즐겨찾기 성공"));
    }

    @DisplayName("음식점 즐겨찾기 추가 토글 테스트 - 즐겨찾기 해제")
    @Test
    public void testToggleHeart_Remove() throws Exception {
        Long mockStoreId = 1L;
        String mockEmail = "nstgic3@gmail.com";

        when(likeStoreService.toggleHeartForStore(mockStoreId, mockEmail)).thenReturn(false);

        UserPrincipal userPrincipal = new UserPrincipal(null, mockEmail, Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST")));

        mockMvc.perform(post("/stores/" + mockStoreId + "/like")
                        .with(oauth2Login().oauth2User(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("즐겨찾기 취소 성공"));
    }



}
