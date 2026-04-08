package com.record.myplace.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "알림 읽음 처리 요청 DTO")
public class UserNotificationReadRequestDto {

    @Schema(description = "알림 ID", example = "1")
    private Long notificationId;
}