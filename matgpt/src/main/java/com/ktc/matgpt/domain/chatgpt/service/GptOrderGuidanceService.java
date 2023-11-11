package com.ktc.matgpt.domain.chatgpt.service;

import com.ktc.matgpt.domain.chatgpt.dto.GptApiRequest;
import com.ktc.matgpt.domain.chatgpt.dto.GptApiResponse;
import com.ktc.matgpt.domain.chatgpt.dto.GptOrderGuidanceResponseDto;
import com.ktc.matgpt.domain.chatgpt.dto.GptRequestConverter;
import com.ktc.matgpt.domain.chatgpt.entity.GptOrderGuidance;
import com.ktc.matgpt.domain.chatgpt.repository.GptOrderGuidanceRepository;
import com.ktc.matgpt.domain.coin.dto.CoinRequest;
import com.ktc.matgpt.domain.coin.service.CoinService;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptOrderGuidanceService {

    private final GptOrderGuidanceRepository gptOrderGuidanceRepository;
    private final UserService userService;
    private final CoinService coinService;
    private final GptApiService gptApiService;

    private static final int DEFAULT_COIN_USAGE = 20;

    @Transactional
    public String generateOrderGuidance(Long userId) throws ExecutionException, InterruptedException {
        User user = userService.findById(userId);
        coinService.useCoin(userId,new CoinRequest.UseCoinDto(DEFAULT_COIN_USAGE));

        GptApiRequest requestBody = GptRequestConverter.convertFromLocale(user.getLocale().getCountryDescription());

        CompletableFuture<GptApiResponse> completableGptApiResponse = gptApiService.callChatGptApi(requestBody);
        GptApiResponse gptApiResponse = completableGptApiResponse.get();

        GptOrderGuidance gptOrderGuidance = GptOrderGuidance.create(user, gptApiResponse.getContent(), gptApiResponse.getCreatedLocalDateTime());
        gptOrderGuidanceRepository.save(gptOrderGuidance);

        return gptOrderGuidance.getContent();
    }

    @Transactional(readOnly = true)
    public List<GptOrderGuidanceResponseDto> getOrderGuidances(Long userId) {
        List<GptOrderGuidance> gptOrderGuidances = gptOrderGuidanceRepository.findAllByUserId(userId);
        return gptOrderGuidances.stream()
                .map(GptOrderGuidanceResponseDto::new)
                .toList();
    }
}
