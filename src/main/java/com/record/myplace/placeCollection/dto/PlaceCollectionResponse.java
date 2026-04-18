package com.record.myplace.placeCollection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "저장 리스트 응답 DTO")
public class PlaceCollectionResponse {

    @Schema(description = "리스트 ID", example = "1")
    private Long collectionId;

    @Schema(description = "리스트 이름", example = "치킨")
    private String name;

    @Schema(description = "리스트에 저장된 식당 수", example = "4")
    private Long placeCount;

    @Schema(description = "현재 식당이 이 리스트에 저장되었는지 여부", example = "true")
    private Boolean saved;

    @Schema(description = "리스트 생성 일시", example = "2026-04-16T10:20:30")
    private LocalDateTime createdAt;

    @Schema(description = "리스트 수정 일시", example = "2026-04-16T10:20:30")
    private LocalDateTime updatedAt;
}
