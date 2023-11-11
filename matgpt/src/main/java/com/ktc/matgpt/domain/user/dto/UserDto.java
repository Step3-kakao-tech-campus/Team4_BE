package com.ktc.matgpt.domain.user.dto;

import com.ktc.matgpt.domain.user.entity.AgeGroup;
import com.ktc.matgpt.domain.user.entity.Gender;
import com.ktc.matgpt.domain.user.entity.LocaleEnum;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.security.dto.AuthDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

public class UserDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String nickname;
        private String gender;
        private int age;
        private String locale;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImageRequest {
        private String email;
        private String presignedUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String email;
        private String nickname;
        private String profileImageUrl;
        private String gender;
        private String age;
        private String locale;
        public static UserDto.Response toDto(User user){
            return new UserDto.Response(
                    user.getEmail(),
                    user.getName(),
                    user.getProfileImageUrl(),
                    Optional.ofNullable(user.getGender()).map(Enum::toString).orElse(null),
                    Optional.ofNullable(user.getAgeGroup()).map(Enum::toString).orElse(null),
                    Optional.ofNullable(user.getLocale()).map(Enum::name).orElse(null)
            );
        }

    }


}
