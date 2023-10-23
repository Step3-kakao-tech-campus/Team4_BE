package com.ktc.matgpt.storeHeart;


import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.security.oauth2.MatgptOAuth2Provider;
import com.ktc.matgpt.user.entity.AgeGroup;
import com.ktc.matgpt.user.entity.Gender;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.repository.UserRepository;
import com.ktc.matgpt.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class HeartRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @BeforeEach
    public void setup() {
        User user = new User();
        user.setAgeGroup(AgeGroup.TWENTY_TO_TWENTYNINE);
        user.setEmailVerified(null);
        user.setId(1L);
        user.setEmail("nstgic3@gmail.com");
        user.setGender(Gender.MALE);
        user.setName("ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7");
        user.setProvider(MatgptOAuth2Provider.KAKAO);
        user.setProviderId("3038773712L");

        userRepository.save(user);

    }

    @DisplayName("Find all liked stores")
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

    @DisplayName("Add heart to store")
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

}
