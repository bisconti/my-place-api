package com.record.myplace.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "사용자 선호 카테고리 DTO")
public class UserPreferredCategoryResponseDto {

    @Schema(description = "사용자 이메일", example = "test@example.com")
    private String userEmail;

    @Schema(description = "선호 카테고리", example = "치킨")
    private String category;

    @Schema(description = "카테고리 선호 점수", example = "12")
    private Integer score;
}