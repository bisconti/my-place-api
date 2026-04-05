package com.record.myplace.placeVisitHistory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "해당 식당 방문 여부 응답 DTO")
public class PlaceVisitedResponseDto {

    @Schema(description = "방문 여부", example = "true")
    private Boolean visited;
}