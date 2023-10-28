package com.ktc.matgpt.user.controller;

import com.ktc.matgpt.security.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Oauth2 Principle 적재 테스트")
    @Test()
    public void getUserInfo_ShouldReturnUserInfo() throws Exception {
        Long mockUserId = 2L;
        String mockUsername = "test@example.com";

        UserPrincipal userPrincipal = new UserPrincipal(mockUserId,mockUsername, Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST")));

        mockMvc.perform(get("/auth/userinfo")
                        .with(oauth2Login().oauth2User(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User info: " + mockUsername));
    }
}
