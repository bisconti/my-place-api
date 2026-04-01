package com.record.myplace.placeReview.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "리뷰 조회 항목 DTO")
public class PlaceReviewItemDto {

    @Schema(description = "리뷰 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이메일", example = "test@example.com")
    private String userEmail;

    @Schema(description = "작성자 닉네임", example = "준민")
    private String nickname;

    @Schema(description = "장소 ID", example = "PLACE_001")
    private String placeId;

    @Schema(description = "장소명", example = "교촌치킨 수지점")
    private String placeName;

    @Schema(description = "별점", example = "5")
    private Integer rating;

    @Schema(description = "리뷰 내용", example = "치킨이 바삭하고 정말 맛있었어요.")
    private String content;

    @Schema(description = "생성일시", example = "2026-04-01T12:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2026-04-01T13:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "리뷰 이미지 목록")
    private List<PlaceReviewImageResponseDto> images;
}