package com.record.myplace.placeCollection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "저장 리스트 생성 요청 DTO")
public class PlaceCollectionCreateRequest {

    @Schema(description = "리스트 이름", example = "치킨")
    private String name;

    @Schema(description = "리스트 색상", example = "#dc2626")
    private String color;
}
