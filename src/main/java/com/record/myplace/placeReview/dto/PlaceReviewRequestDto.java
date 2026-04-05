package com.record.myplace.placeReview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "리뷰 등록 요청 DTO")
public class PlaceReviewRequestDto {

    @Schema(description = "사용자 이메일", example = "test@example.com")
    private String userEmail;

    @Schema(description = "장소 ID", example = "KAKAO_123")
    private String placeId;

    @Schema(description = "장소명", example = "교촌치킨 수지점")
    private String placeName;

    @Schema(description = "지번 주소", example = "경기도 용인시 수지구 ...")
    private String address;

    @Schema(description = "도로명 주소", example = "경기도 용인시 수지구 포은대로 ...")
    private String roadAddress;

    @Schema(description = "카테고리", example = "치킨")
    private String category;

    @Schema(description = "전화번호", example = "031-123-4567")
    private String phone;

    @Schema(description = "별점", example = "5")
    private Integer rating;

    @Schema(description = "리뷰 내용", example = "음식이 맛있고 분위기가 좋았습니다.")
    private String content;
}