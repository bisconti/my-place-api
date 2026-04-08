package com.record.myplace.notification.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.notification.dto.UnreadNotificationCountResponseDto;
import com.record.myplace.notification.dto.UserNotificationResponseDto;
import com.record.myplace.notification.service.UserNotificationQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "User Notification Query", description = "사용자 알림 조회 API")
public class UserNotificationQueryController {

    private final UserNotificationQueryService userNotificationQueryService;

    @Operation(summary = "알림 목록 조회")
    @GetMapping
    public List<UserNotificationResponseDto> getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return userNotificationQueryService.getNotifications(userDetails.getEmail());
    }

    @Operation(summary = "읽지 않은 알림 개수 조회")
    @GetMapping("/unread-count")
    public UnreadNotificationCountResponseDto getUnreadNotificationCount(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return userNotificationQueryService.getUnreadNotificationCount(userDetails.getEmail());
    }
}