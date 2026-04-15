package com.record.myplace.recentplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RecentPlaceQueryDto {

    @Schema(description = "식당 ID")
    private String placeId;

    @Schema(description = "식당명")
    private String placeName;

    @Schema(description = "대표 이미지 경로")
    private String thumbnail;

    @Schema(description = "평점")
    private Double rating;

    @Schema(description = "리뷰 수")
    private Integer reviewCount;

    @Schema(description = "조회 시간")
    private String viewedAt;
}
