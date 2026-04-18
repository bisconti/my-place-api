package com.record.myplace.placeCollection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "저장 리스트에 식당 저장 요청 DTO")
public class PlaceCollectionSavePlaceRequest {

    @Schema(description = "식당 ID", example = "82522548")
    private String placeId;

    @Schema(description = "식당명", example = "쿠우쿠우 분당점")
    private String placeName;

    @Schema(description = "지번 주소", example = "경기 성남시 분당구 구미동 159")
    private String address;

    @Schema(description = "도로명 주소", example = "경기 성남시 분당구 탄천상로151번길 20")
    private String roadAddress;

    @Schema(description = "카테고리", example = "초밥")
    private String category;

    @Schema(description = "전화번호", example = "031-728-5353")
    private String phone;
}
