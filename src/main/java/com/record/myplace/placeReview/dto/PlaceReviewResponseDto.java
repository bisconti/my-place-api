package com.record.myplace.placeReview.dto;

import java.time.LocalDateTime;

import com.record.myplace.placeReview.entity.PlaceReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PlaceReviewResponseDto {

    private Long id;
    private String userEmail;
    private String placeId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PlaceReviewResponseDto fromEntity(PlaceReview entity) {
        return PlaceReviewResponseDto.builder()
                .id(entity.getId())
                .userEmail(entity.getUserEmail())
                .placeId(entity.getPlaceId())
                .rating(entity.getRating())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
