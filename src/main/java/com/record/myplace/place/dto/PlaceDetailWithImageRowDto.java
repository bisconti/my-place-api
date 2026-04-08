package com.record.myplace.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "식당 상세 + 이미지 JOIN 조회 Row DTO")
public class PlaceDetailWithImageRowDto {

    @Schema(description = "식당 ID", example = "12345")
    private String placeId;

    @Schema(description = "식당명", example = "맛있는 치킨집")
    private String placeName;

    @Schema(description = "주소", example = "경기도 용인시 수지구 동천동 123-1")
    private String address;

    @Schema(description = "도로명 주소", example = "경기도 용인시 수지구 손곡로 123")
    private String roadAddress;

    @Schema(description = "카테고리", example = "치킨")
    private String category;

    @Schema(description = "전화번호", example = "031-123-4567")
    private String phone;

    @Schema(description = "위도", example = "37.335123")
    private Double latitude;

    @Schema(description = "경도", example = "127.095123")
    private Double longitude;

    @Schema(description = "찜 여부", example = "true")
    private Boolean liked;

    @Schema(description = "현재 위치 기준 거리(m)", example = "350")
    private Integer distanceM;

    @Schema(description = "이미지 ID", example = "1")
    private Long imageId;

    @Schema(description = "원본 파일명", example = "store1.jpg")
    private String originalFileName;

    @Schema(description = "저장 파일명", example = "8f3a1c-store1.jpg")
    private String storedFileName;

    @Schema(description = "파일 경로", example = "/uploads/places/8f3a1c-store1.jpg")
    private String filePath;

    @Schema(description = "파일 크기(byte)", example = "123456")
    private Long fileSize;

    @Schema(description = "정렬 순서", example = "1")
    private Integer sortOrder;
}