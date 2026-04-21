package com.record.myplace.place.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "식당 목록 메타데이터 조회 요청 DTO")
public class PlaceListMetadataRequestDto {

    @Schema(description = "메타데이터를 조회할 식당 ID 목록")
    private List<String> placeIds = new ArrayList<>();
}
