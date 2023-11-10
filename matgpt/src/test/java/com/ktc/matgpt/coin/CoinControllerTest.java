package com.ktc.matgpt.coin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.domain.coin.dto.CoinRequest;
import com.ktc.matgpt.domain.coin.entity.Coin;
import com.ktc.matgpt.domain.coin.repository.CoinRepository;
import com.ktc.matgpt.security.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({ SpringExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class CoinControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("코인 잔액 확인 테스트")
    @Test
    void getCoinBalance_test() throws Exception {

        // given
        Long userId = 1L;
        int lastBalance = 10;
        UserPrincipal mockUserPrincipal = new UserPrincipal(userId, "nstgic3@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        Coin coin = coinRepository.findByUserId(userId).get();
        coin.setBalance(lastBalance);

        // when
        ResultActions resultActions = mvc.perform(
                get("/coin")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.userId").value(userId));
        resultActions.andExpect(jsonPath("$.data.balance").value(lastBalance));
    }

    @DisplayName("코인 획득 테스트")
    @Transactional
    @Test
    void earnCoin_test() throws Exception {

        // given
        Long userId = 1L;
        int lastBalance = 1000;
        int amount = 1000;
        UserPrincipal mockUserPrincipal = new UserPrincipal(userId, "nstgic3@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        Coin coin = coinRepository.findByUserId(userId).get();
        coin.setBalance(lastBalance);

        CoinRequest.EarnCoinDto earnCoinDto = new CoinRequest.EarnCoinDto(amount);
        String content = objectMapper.writeValueAsString(earnCoinDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/coin/earn")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
                        .content(content)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()
                );

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.userId").value(userId));
        resultActions.andExpect(jsonPath("$.data.balance").value(lastBalance + amount));

    }

    @DisplayName("코인 사용 실패 - 잔액 부족")
    @Transactional
    @Test
    void useCoin_fail_test() throws Exception {

        // given
        // 0원을 이미 가지고 있었다고 가정
        Long userId = 1L;
        int lastBalance = 0;
        int amount = 1000;
        UserPrincipal mockUserPrincipal = new UserPrincipal(userId, "nstgic3@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        Coin coin = coinRepository.findByUserId(userId).get();
        coin.setBalance(lastBalance);

        CoinRequest.UseCoinDto useCoinDto = new CoinRequest.UseCoinDto(amount);
        String content = objectMapper.writeValueAsString(useCoinDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/coin/use")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
                        .content(content)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()
                );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("코인 사용 성공 테스트")
    @Transactional
    @Test
    void useCoin_success_test() throws Exception {

        // given
        // 1000원을 이미 가지고 있었다고 가정
        Long userId = 1L;
        int lastBalance = 1000;
        int amount = 1000;
        UserPrincipal mockUserPrincipal = new UserPrincipal(userId, "nstgic3@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        Coin coin = coinRepository.findByUserId(userId).get();
        coin.setBalance(lastBalance);

        CoinRequest.UseCoinDto useCoinDto = new CoinRequest.UseCoinDto(amount);
        String content = objectMapper.writeValueAsString(useCoinDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/coin/use")
                                .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
                                .content(content)
                                .characterEncoding("UTF-8")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()
                );

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.userId").value(userId));
        resultActions.andExpect(jsonPath("$.data.balance").value(lastBalance - amount));
    }
}