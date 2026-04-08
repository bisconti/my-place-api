package com.record.myplace.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "식당 이미지 정보 DTO")
public class PlaceImageResponseDto {

    @Schema(description = "이미지 ID", example = "1")
    private Long id;

    @Schema(description = "식당 ID", example = "12345")
    private String placeId;

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