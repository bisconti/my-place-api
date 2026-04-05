package com.record.myplace.placeVisitHistory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "방문 기록 등록 요청 DTO")
public class PlaceVisitHistoryCreateRequestDto {

    @Schema(description = "식당 ID", example = "place_1001")
    private String placeId;

    @Schema(description = "방문일자(yyyy-MM-dd)", example = "2026-04-03")
    private String visitDate;
}