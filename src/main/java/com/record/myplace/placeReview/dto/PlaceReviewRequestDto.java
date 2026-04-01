package com.record.myplace.placeReview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "리뷰 등록 요청 DTO")
public class PlaceReviewRequestDto {

    @Schema(description = "사용자 이메일", example = "test@example.com")
    private String userEmail;

    @Schema(description = "장소 ID", example = "PLACE_001")
    private String placeId;

    @Schema(description = "별점", example = "5")
    private Integer rating;

    @Schema(description = "리뷰 내용", example = "음식이 맛있고 분위기가 좋았습니다.")
    private String content;
}