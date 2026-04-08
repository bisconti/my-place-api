package com.record.myplace.notification.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "리뷰 리마인드 알림 생성 대상 DTO")
public class ReviewReminderTargetResponseDto {

    @Schema(description = "사용자 이메일", example = "test@test.com")
    private String userEmail;

    @Schema(description = "식당 ID", example = "place_1001")
    private String placeId;

    @Schema(description = "식당명", example = "맛있는 식당")
    private String placeName;

    @Schema(description = "방문일")
    private LocalDate visitDate;
}