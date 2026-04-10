package com.record.myplace.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "추천 식당 DTO")
public class RecommendedPlaceResponseDto {

    @Schema(description = "식당 ID", example = "8059431")
    private String placeId;

    @Schema(description = "식당명", example = "맛있는 식당")
    private String placeName;

    @Schema(description = "카테고리", example = "치킨")
    private String category;

    @Schema(description = "주소", example = "경기도 성남시 분당구 ...")
    private String address;

    @Schema(description = "평균 평점", example = "4.5")
    private Double averageRating;

    @Schema(description = "리뷰 수", example = "12")
    private Integer reviewCount;
}