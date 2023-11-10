package com.ktc.matgpt.security.oauth2;

import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.security.jwt.TokenProvider;
import com.ktc.matgpt.domain.user.entity.RefreshToken;
import com.ktc.matgpt.domain.user.repository.RefreshTokenRepository;
import com.ktc.matgpt.domain.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var token = tokenProvider.generateToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        refreshTokenRepository.save(RefreshToken.create(authentication.getName(), token.getRefreshToken()));

        // 만약 첫 로그인이라면 isFirstLogin 플래그를 false로 업데이트합니다.
        if (userPrincipal.isFirstLogin()) {
            userService.completeRegistration(userPrincipal.getId());
        }
        // 클라이언트로 리디렉트할 URL을 설정합니다. 이 URL은 프론트엔드 측에 맞게 설정해야 합니다.
        String redirectUrl = "http://localhost:3000/login-success";

        // URL 파라미터로 토큰 정보를 전달합니다.
        redirectUrl += "&isFirstLogin=" + userPrincipal.isFirstLogin();
        redirectUrl += "&accessToken=" + URLEncoder.encode(token.getAccessToken(), StandardCharsets.UTF_8.toString());
        redirectUrl += "&accessTokenExpiresIn=" + token.getAccessTokenExpiresIn();
        redirectUrl += "&refreshToken=" + URLEncoder.encode(token.getRefreshToken(), StandardCharsets.UTF_8.toString());

        // 클라이언트로 리디렉트합니다.
        response.sendRedirect(redirectUrl);
    }

}
