package com.record.myplace.place.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "식당 상세 조회 응답 DTO")
public class PlaceDetailResponseDto {

    @Schema(description = "식당 ID", example = "8059431")
    private String id;

    @Schema(description = "식당명", example = "맛있는 식당")
    private String name;

    @Schema(description = "지번 주소", example = "경기도 성남시 분당구 ...")
    private String address;

    @Schema(description = "도로명 주소", example = "경기도 성남시 분당구 ...")
    private String roadAddress;

    @Schema(description = "카테고리", example = "한식")
    private String category;

    @Schema(description = "전화번호", example = "031-123-4567")
    private String phone;

    @Schema(description = "위도", example = "37.123456")
    private Double lat;

    @Schema(description = "경도", example = "127.123456")
    private Double lng;

    @Schema(description = "현재 사용자의 찜 여부", example = "false")
    private Boolean liked;

    @Schema(description = "현재 위치 기준 거리(m)", example = "0")
    private Integer distanceM;
    
    @Schema(description = "식당 이미지 목록")
    private List<PlaceImageResponseDto> images;
}