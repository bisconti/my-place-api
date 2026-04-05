package com.record.myplace.settings.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.settings.dto.NotificationSettingsResponseDto;
import com.record.myplace.settings.service.UserNotificationSettingCommandService;
import com.record.myplace.settings.service.UserNotificationSettingQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/settings/notifications")
@RequiredArgsConstructor
@Tag(name = "User Notification Setting Query", description = "사용자 알림 설정 조회 API")
public class UserNotificationSettingQueryController {

    private final UserNotificationSettingQueryService userNotificationSettingQueryService;
    private final UserNotificationSettingCommandService userNotificationSettingCommandService;

    @Operation(summary = "알림 설정 조회", description = "현재 로그인한 사용자의 알림 설정을 조회합니다.")
    @GetMapping
    public NotificationSettingsResponseDto getNotificationSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String userEmail = userDetails.getEmail();

        userNotificationSettingCommandService.createDefaultNotificationSettingsIfNotExists(userEmail);

        return userNotificationSettingQueryService.getNotificationSettings(userEmail);
    }
}
