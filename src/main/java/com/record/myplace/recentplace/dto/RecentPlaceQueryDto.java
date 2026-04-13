package com.record.myplace.recentplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RecentPlaceQueryDto {

    @Schema(description = "식당 ID")
    private String placeId;

    @Schema(description = "조회 시간")
    private String viewedAt;
}