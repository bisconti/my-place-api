package com.record.myplace.settings.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "알림 설정 조회 응답 DTO")
public class NotificationSettingsResponseDto {

    @Schema(description = "알림 설정 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이메일", example = "test@test.com")
    private String userEmail;

    @Schema(description = "리뷰 작성 리마인드 알림 여부", example = "true")
    private Boolean reviewReminderEnabled;

    @Schema(description = "찜한 맛집 소식 알림 여부", example = "true")
    private Boolean favoritePlaceEventEnabled;

    @Schema(description = "맞춤 추천 알림 여부", example = "true")
    private Boolean recommendationEnabled;

    @Schema(description = "방문 기록 리마인드 알림 여부", example = "false")
    private Boolean visitReminderEnabled;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;
}