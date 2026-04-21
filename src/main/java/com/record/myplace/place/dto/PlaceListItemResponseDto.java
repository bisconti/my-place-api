package com.record.myplace.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "식당 목록 메타데이터 응답 DTO")
public class PlaceListItemResponseDto {

    @Schema(description = "식당 ID", example = "82522548")
    private String placeId;

    @Schema(description = "대표 썸네일 경로", example = "/uploads/places/thumb.jpg")
    private String thumbnail;

    @Schema(description = "평균 별점", example = "4.4")
    private Double rating;

    @Schema(description = "리뷰 수", example = "18")
    private Long reviewCount;

    @Schema(description = "찜 수", example = "27")
    private Long likeCount;

    @Schema(description = "현재 사용자의 찜 여부", example = "true")
    private Boolean liked;

    @Schema(description = "생생정보통TV 출연 여부", example = "false")
    private Boolean featuredLiveInfoTv;

    @Schema(description = "생활의 달인 출연 여부", example = "true")
    private Boolean featuredLifeMaster;

    @Schema(description = "백반기행 출연 여부", example = "false")
    private Boolean featuredBaekbanTrip;
}
