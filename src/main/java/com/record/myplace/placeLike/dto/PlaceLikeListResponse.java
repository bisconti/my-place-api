package com.record.myplace.placeLike.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "내 찜 목록 응답 DTO")
public class PlaceLikeListResponse {

    @Schema(description = "찜 목록")
    private List<PlaceLikeResponse> items;
}