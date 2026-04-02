package com.record.myplace.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.com.dto.MessageResponse;
import com.record.myplace.user.dto.ChangePasswordRequest;
import com.record.myplace.user.dto.UpdateProfileResponse;
import com.record.myplace.user.service.UserCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Command", description = "사용자 변경 API")
public class UserCommandController {

    private final UserCommandService userCommandService;

    @Operation(summary = "프로필 수정", description = "로그인한 사용자의 닉네임과 자기소개를 수정합니다.")
    @PutMapping("/me")
    public ResponseEntity<UpdateProfileResponse> updateMe(
            @RequestBody Map<String, String> req,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                userCommandService.updateMe(
                        user.getEmail(),
                        req.get("nickname"),
                        req.get("bio")
                )
        );
    }

    @Operation(summary = "비밀번호 변경", description = "로그인한 사용자의 비밀번호를 변경합니다.")
    @PutMapping("/me/password")
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest req,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                userCommandService.changePassword(
                        user.getEmail(),
                        req.getCurrentPassword(),
                        req.getNewPassword()
                )
        );
    }
}