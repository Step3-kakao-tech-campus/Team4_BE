package com.ktc.matgpt.user.service;

import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;
import com.ktc.matgpt.security.jwt.TokenDto;
import com.ktc.matgpt.user.entity.RefreshToken;
import com.ktc.matgpt.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void verifyAndDeleteRefreshToken(String key, TokenDto.Request tokenRequest) {
        // 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(key)
                .orElseThrow(() -> new CustomException(ErrorCode.LOGOUT_USER_NOT_FOUND));

        // Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequest.getRefreshToken())) {
            throw new CustomException(ErrorCode.TOKEN_MISMATCH_EXCEPTION);
        }

        // 토큰 삭제 로직 (필요에 따라 구현)
        refreshTokenRepository.delete(refreshToken);
    }

}
