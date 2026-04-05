package com.record.myplace.settings.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.settings.dto.NotificationSettingsRequestDto;
import com.record.myplace.settings.dto.NotificationSettingsResponseDto;
import com.record.myplace.settings.service.UserNotificationSettingCommandService;
import com.record.myplace.settings.service.UserNotificationSettingQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/settings/notifications")
@RequiredArgsConstructor
@Tag(name = "User Notification Setting Command", description = "사용자 알림 설정 저장 API")
public class UserNotificationSettingCommandController {

    private final UserNotificationSettingCommandService userNotificationSettingCommandService;
    private final UserNotificationSettingQueryService userNotificationSettingQueryService;

    @Operation(summary = "알림 설정 저장", description = "현재 로그인한 사용자의 알림 설정을 저장합니다.")
    @PutMapping
    public NotificationSettingsResponseDto saveNotificationSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NotificationSettingsRequestDto requestDto
    ) {
        String userEmail = userDetails.getEmail();

        userNotificationSettingCommandService.saveNotificationSettings(userEmail, requestDto);

        return userNotificationSettingQueryService.getNotificationSettings(userEmail);
    }
}
