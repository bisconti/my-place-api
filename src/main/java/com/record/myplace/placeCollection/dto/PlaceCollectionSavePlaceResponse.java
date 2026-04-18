package com.record.myplace.placeCollection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "저장 리스트에 식당 저장 응답 DTO")
public class PlaceCollectionSavePlaceResponse {

    @Schema(description = "리스트 ID", example = "1")
    private Long collectionId;

    @Schema(description = "식당 ID", example = "82522548")
    private String placeId;

    @Schema(description = "저장 완료 여부", example = "true")
    private Boolean saved;

    @Schema(description = "저장 일시", example = "2026-04-16T10:20:30")
    private LocalDateTime savedAt;
}
