package com.record.myplace.placeLike.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "찜 토글 요청 DTO")
public class PlaceLikeToggleRequest {

    @Schema(description = "장소 ID", example = "KAKAO_123")
    private String placeId;

    @Schema(description = "장소명", example = "교촌치킨 수지점")
    private String placeName;

    @Schema(description = "주소", example = "경기도 용인시 수지구 ...")
    private String address;

    @Schema(description = "카테고리", example = "치킨")
    private String category;

    @Schema(description = "찜 여부", example = "true")
    private Boolean liked;
}