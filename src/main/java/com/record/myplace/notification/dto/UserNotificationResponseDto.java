package com.record.myplace.notification.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "사용자 알림 응답 DTO")
public class UserNotificationResponseDto {

    @Schema(description = "알림 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이메일", example = "test@test.com")
    private String userEmail;

    @Schema(description = "알림 타입", example = "REVIEW_REMINDER")
    private String notificationType;

    @Schema(description = "알림 제목", example = "리뷰를 남겨보세요")
    private String title;

    @Schema(description = "알림 내용", example = "가봤던 맛집에 아직 리뷰가 없어요.")
    private String content;

    @Schema(description = "대상 ID", example = "place_1001")
    private String targetId;

    @Schema(description = "대상 타입", example = "PLACE")
    private String targetType;

    @Schema(description = "읽음 여부", example = "false")
    private Boolean isRead;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "읽음일시")
    private LocalDateTime readAt;
}