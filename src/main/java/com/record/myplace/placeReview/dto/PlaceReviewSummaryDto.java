package com.record.myplace.placeReview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "장소별 리뷰 요약 DTO")
public class PlaceReviewSummaryDto {

    @Schema(description = "장소 ID", example = "PLACE_001")
    private String placeId;

    @Schema(description = "평균 별점", example = "4.5")
    private Double averageRating;

    @Schema(description = "리뷰 개수", example = "12")
    private Long reviewCount;
}