package com.record.myplace.placeLike.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "찜 응답 DTO")
public class PlaceLikeResponse {

    @Schema(description = "장소 ID", example = "KAKAO_123")
    private String placeId;

    @Schema(description = "장소명", example = "교촌치킨 수지점")
    private String placeName;

    @Schema(description = "주소", example = "경기도 용인시 수지구 ...")
    private String address;

    @Schema(description = "카테고리", example = "치킨")
    private String category;

    @Schema(description = "찜 생성일시", example = "2026-04-02T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "찜 여부", example = "true")
    private Boolean liked;
}