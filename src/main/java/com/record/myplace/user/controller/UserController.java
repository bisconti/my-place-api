package com.record.myplace.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.com.dto.MessageResponse;
import com.record.myplace.user.dto.ChangePasswordRequest;
import com.record.myplace.user.dto.MeResponse;
import com.record.myplace.user.dto.UpdateProfileResponse;
import com.record.myplace.user.service.UserService;

import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // 프로필 조회
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(userService.getMe(user.getEmail()));
    }

    // 프로필 수정
    @PutMapping("/me")
    public ResponseEntity<UpdateProfileResponse> updateMe(
            @RequestBody Map<String, String> req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                userService.updateMe(
                        user.getEmail(),
                        req.get("nickname"),
                        req.get("bio")
                )
        );
    }

    // 비밀번호 변경
    @PutMapping("/me/password")
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                userService.changePassword(
                		user.getEmail(),
                        req.getCurrentPassword(),
                        req.getNewPassword()
                )
        );
    }
}

