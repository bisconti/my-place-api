package com.record.myplace.placeReview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PlaceReviewSummaryDto {

    private String placeId;
    private Double averageRating;
    private Long reviewCount;
}