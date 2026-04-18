package com.record.myplace.placeCollection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "저장 리스트 상세 응답 DTO")
public class PlaceCollectionDetailResponse {

    @Schema(description = "리스트 ID", example = "1")
    private Long collectionId;

    @Schema(description = "리스트 이름", example = "치킨")
    private String name;

    @Schema(description = "저장된 식당 수", example = "4")
    private Long placeCount;

    @Schema(description = "리스트 생성 일시", example = "2026-04-16T10:20:30")
    private LocalDateTime createdAt;

    @Schema(description = "리스트 수정 일시", example = "2026-04-16T10:20:30")
    private LocalDateTime updatedAt;

    @Schema(description = "리스트에 저장된 식당 목록")
    private List<PlaceCollectionItemResponse> items = new ArrayList<>();
}
