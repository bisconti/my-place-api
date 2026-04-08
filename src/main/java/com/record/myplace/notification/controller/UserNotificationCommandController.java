package com.record.myplace.notification.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.notification.service.UserNotificationCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "User Notification Command", description = "사용자 알림 명령 API")
public class UserNotificationCommandController {

    private final UserNotificationCommandService userNotificationCommandService;

    @Operation(summary = "알림 읽음 처리")
    @PutMapping("/{notificationId}/read")
    public void markAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long notificationId
    ) {
        userNotificationCommandService.markAsRead(userDetails.getEmail(), notificationId);
    }

    @Operation(summary = "전체 알림 읽음 처리")
    @PutMapping("/read-all")
    public void markAllAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userNotificationCommandService.markAllAsRead(userDetails.getEmail());
    }
}