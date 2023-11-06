package com.ktc.matgpt.auth;


import com.ktc.matgpt.security.jwt.TokenDto;
import com.ktc.matgpt.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenDto.Request requestDto, HttpServletRequest request) {
        authService.logout(requestDto, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto.Response> reissue(@RequestBody TokenDto.Request requestDto, HttpServletRequest request) {
        return ResponseEntity.ok(authService.reissue(requestDto, request));
    }

}
