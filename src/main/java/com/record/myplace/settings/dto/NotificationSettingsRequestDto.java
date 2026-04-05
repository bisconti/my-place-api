package com.record.myplace.settings.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "알림 설정 저장 요청 DTO")
public class NotificationSettingsRequestDto {

    @Schema(description = "리뷰 작성 리마인드 알림 여부", example = "true")
    private Boolean reviewReminderEnabled;

    @Schema(description = "찜한 맛집 소식 알림 여부", example = "true")
    private Boolean favoritePlaceEventEnabled;

    @Schema(description = "맞춤 추천 알림 여부", example = "true")
    private Boolean recommendationEnabled;

    @Schema(description = "방문 기록 리마인드 알림 여부", example = "false")
    private Boolean visitReminderEnabled;
}