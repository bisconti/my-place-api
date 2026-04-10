package com.record.myplace.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "추천 알림 대상 사용자 DTO")
public class RecommendationNotificationTargetUserResponseDto {

    @Schema(description = "사용자 이메일", example = "test@example.com")
    private String userEmail;
}