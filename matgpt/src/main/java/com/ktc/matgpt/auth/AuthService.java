package com.ktc.matgpt.auth;

import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;
import com.ktc.matgpt.security.jwt.TokenDto;
import com.ktc.matgpt.security.jwt.TokenProvider;
import com.ktc.matgpt.user.entity.RefreshToken;
import com.ktc.matgpt.user.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void logout(TokenDto.Request tokenRequest, HttpServletRequest request) {
        refreshTokenRepository.deleteByKey(tokenProvider.getAuthentication(tokenRequest.getAccessToken(), request).getName());
    }

    @Transactional
    public TokenDto.Response reissue(TokenDto.Request tokenRequest, HttpServletRequest request) {
        validateRefreshToken(tokenRequest.getRefreshToken());

        Authentication authentication = tokenProvider.getAuthentication(tokenRequest.getAccessToken(), request);
        RefreshToken refreshToken = getRefreshToken(authentication.getName());
        validateTokenMatch(refreshToken, tokenRequest.getRefreshToken());

        TokenDto.Response tokenResponse = tokenProvider.generateToken(authentication);
        updateRefreshToken(refreshToken, tokenResponse.getRefreshToken());

        return tokenResponse;
    }

    private void validateRefreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION);
        }
    }

    private RefreshToken getRefreshToken(String memberId) {
        return refreshTokenRepository.findByKey(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.LOGOUT_USER_NOT_FOUND));
    }

    private void validateTokenMatch(RefreshToken storedToken, String providedToken) {
        if (!storedToken.getValue().equals(providedToken)) {
            throw new CustomException(ErrorCode.TOKEN_MISMATCH_EXCEPTION);
        }
    }

    private void updateRefreshToken(RefreshToken refreshToken, String newTokenValue) {
        refreshToken.updateValue(newTokenValue);
        refreshTokenRepository.save(refreshToken);
    }


}
