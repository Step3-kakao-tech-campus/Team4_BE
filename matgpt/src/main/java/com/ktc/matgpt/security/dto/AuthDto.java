package com.ktc.matgpt.security.dto;

import com.ktc.matgpt.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DefaultRequest {

        @NotNull
        @Size(min = 4,max = 15)
        private String email;

        @NotNull
        @Size(min = 8,max = 20)
        private String password;

        public User toEntity(PasswordEncoder passwordEncoder) {
            return User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
        }

        public static DefaultRequest toDto(User user){
            return new DefaultRequest(
                    user.getEmail(),
                    user.getPassword()
            );
        }

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);

        }
    }


}
