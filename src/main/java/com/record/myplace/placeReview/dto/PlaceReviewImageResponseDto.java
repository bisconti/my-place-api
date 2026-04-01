package com.record.myplace.placeReview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "리뷰 이미지 응답 DTO")
public class PlaceReviewImageResponseDto {

    @Schema(description = "이미지 ID", example = "10")
    private Long id;

    @Schema(description = "리뷰 ID", example = "1")
    private Long reviewId;

    @Schema(description = "원본 파일명", example = "food.jpg")
    private String originalFileName;

    @Schema(description = "저장 파일명", example = "a12b34c5-d678-90ef-gh12-345678ijklmn.jpg")
    private String storedFileName;

    @Schema(description = "파일 경로", example = "/uploads/reviews/a12b34c5-d678-90ef-gh12-345678ijklmn.jpg")
    private String filePath;

    @Schema(description = "파일 크기(byte)", example = "204800")
    private Long fileSize;

    @Schema(description = "정렬 순서", example = "1")
    private Integer sortOrder;
}