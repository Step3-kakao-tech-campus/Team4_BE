package com.ktc.matgpt.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.security.jwt.TokenProvider;
import com.ktc.matgpt.user.entity.RefreshToken;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.repository.RefreshTokenRepository;
import com.ktc.matgpt.user.service.UserService;
import com.ktc.matgpt.utils.ApiUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var token = tokenProvider.generateToken(authentication);
        User user = userService.findById(((User) authentication.getPrincipal()).getId()); // 사용자 정보 획득
        refreshTokenRepository.save(RefreshToken.create(authentication.getName(),token.getRefreshToken()));

        // 응답 구조에 최초 로그인 여부를 boolean 값으로 포함
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        responseData.put("isFirstLogin", user.isFirstLogin()); // 최초 로그인 여부


        response.setStatus(HttpStatus.FOUND.value()); // 상태 코드 변경
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setLocale(Locale.KOREA);
        objectMapper.writeValue(response.getWriter(), ApiUtils.success(responseData)); // 토큰과 리다이렉트 URI를 보냄
        response.getWriter().flush();
    }

}
