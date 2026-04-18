package com.record.myplace.placeCollection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "저장 리스트 식당 항목 응답 DTO")
public class PlaceCollectionItemResponse {

    @Schema(description = "리스트 항목 ID", example = "10")
    private Long collectionItemId;

    @Schema(description = "리스트 ID", example = "1")
    private Long collectionId;

    @Schema(description = "식당 ID", example = "82522548")
    private String placeId;

    @Schema(description = "식당명", example = "쿠우쿠우 분당점")
    private String placeName;

    @Schema(description = "카테고리", example = "초밥")
    private String category;

    @Schema(description = "지번 주소", example = "경기 성남시 분당구 구미동 159")
    private String address;

    @Schema(description = "도로명 주소", example = "경기 성남시 분당구 탄천상로151번길 20")
    private String roadAddress;

    @Schema(description = "썸네일 경로", example = "/uploads/place/thumbnail.jpg")
    private String thumbnail;

    @Schema(description = "평균 별점", example = "4.3")
    private Double rating;

    @Schema(description = "리뷰 수", example = "18")
    private Long reviewCount;

    @Schema(description = "저장 일시", example = "2026-04-16T10:20:30")
    private LocalDateTime savedAt;
}
