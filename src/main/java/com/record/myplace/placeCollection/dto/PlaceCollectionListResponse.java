package com.record.myplace.placeCollection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "저장 리스트 목록 응답 DTO")
public class PlaceCollectionListResponse {

    @Schema(description = "저장 리스트 목록")
    private List<PlaceCollectionResponse> items = new ArrayList<>();
}
