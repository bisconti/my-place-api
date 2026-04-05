package com.record.myplace.placeVisitHistory.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "방문 기록 응답 DTO")
public class PlaceVisitHistoryResponseDto {

    @Schema(description = "방문 기록 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이메일", example = "test@test.com")
    private String userEmail;

    @Schema(description = "식당 ID", example = "place_1001")
    private String placeId;

    @Schema(description = "방문일")
    private LocalDate visitDate;

    @Schema(description = "방문 기록 생성 방식", example = "MANUAL")
    private String visitSource;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;
}