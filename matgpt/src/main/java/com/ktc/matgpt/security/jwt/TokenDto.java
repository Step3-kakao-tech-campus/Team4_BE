package com.ktc.matgpt.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class
TokenDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpiresIn;
    }

}
