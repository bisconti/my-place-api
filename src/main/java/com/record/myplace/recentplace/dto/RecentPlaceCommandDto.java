package com.record.myplace.recentplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RecentPlaceCommandDto {

    @Schema(description = "유저 이메일")
    private String userEmail;

    @Schema(description = "식당 ID")
    private String placeId;
}