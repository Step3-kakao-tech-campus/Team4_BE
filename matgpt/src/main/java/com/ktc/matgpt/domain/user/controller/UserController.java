package com.ktc.matgpt.domain.user.controller;

import com.ktc.matgpt.domain.user.dto.UserDto;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.domain.user.repository.UserRepository;
import com.ktc.matgpt.domain.user.service.UserService;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(ApiUtils.success(UserDto.Response.toDto(userService.findByEmail(userPrincipal.getEmail()))));
    }

    @PostMapping("/info")
    public ResponseEntity<?> editUserInfo(@RequestBody UserDto.Request userDto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.updateUserAdditionalInfo(userPrincipal.getEmail(),userDto);
        return ResponseEntity.ok(ApiUtils.success("유저프로필 업데이트 완료"));
    }






}
