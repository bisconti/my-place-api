package com.record.myplace.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "식당 자동완성 응답 DTO")
public class PlaceAutoCompleteResponseDto {

    @Schema(description = "식당 ID", example = "place_101")
    private String placeId;

    @Schema(description = "식당명", example = "매운치킨 정자점")
    private String placeName;

    @Schema(description = "카테고리", example = "치킨")
    private String category;

    @Schema(description = "도로명 주소", example = "경기도 성남시 분당구 정자동 123")
    private String roadAddress;
}