package com.record.myplace.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "추천 알림 생성 요청 DTO")
public class RecommendationNotificationCreateRequestDto {

    @Schema(description = "사용자 이메일", example = "test@example.com")
    private String userEmail;

    @Schema(description = "추천 식당 ID", example = "8059431")
    private String placeId;

    @Schema(description = "추천 식당명", example = "맛있는 식당")
    private String placeName;

    @Schema(description = "추천 식당 카테고리", example = "치킨")
    private String category;
}