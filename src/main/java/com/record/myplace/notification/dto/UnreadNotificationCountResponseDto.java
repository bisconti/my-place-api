package com.record.myplace.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "읽지 않은 알림 개수 응답 DTO")
public class UnreadNotificationCountResponseDto {

    @Schema(description = "읽지 않은 알림 개수", example = "3")
    private Integer unreadCount;
}