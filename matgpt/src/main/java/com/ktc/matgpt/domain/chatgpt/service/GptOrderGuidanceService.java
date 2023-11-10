package com.ktc.matgpt.domain.chatgpt.service;

import com.ktc.matgpt.domain.chatgpt.dto.GptApiRequest;
import com.ktc.matgpt.domain.chatgpt.dto.GptApiResponse;
import com.ktc.matgpt.domain.chatgpt.dto.GptOrderGuidanceResponseDto;
import com.ktc.matgpt.domain.chatgpt.dto.GptRequestConverter;
import com.ktc.matgpt.domain.chatgpt.entity.GptOrderGuidance;
import com.ktc.matgpt.domain.chatgpt.repository.GptOrderGuidanceRepository;
import com.ktc.matgpt.domain.user.entity.LocaleEnum;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.domain.user.service.UserService;
import com.ktc.matgpt.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptOrderGuidanceService {

    private final GptOrderGuidanceRepository gptOrderGuidanceRepository;
    private final UserService userService;
    private final GptApiService gptApiService;

    @Transactional
    public String generateOrderGuidance(Long userId) throws ExecutionException, InterruptedException {
        User user = userService.findById(userId);
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

    private LocaleEnum getLocaleFromUser(User user) {
        LocaleEnum locale = user.getLocale();
        if (locale == null) {
            throw new NoSuchElementException(ErrorMessage.USER_LOCALE_NOT_FOUND);
        }
        return locale;
    }
}
