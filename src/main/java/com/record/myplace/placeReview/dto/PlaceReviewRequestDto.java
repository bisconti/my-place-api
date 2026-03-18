package com.record.myplace.placeReview.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaceReviewRequestDto {

    private String userEmail;
    private String placeId;
    private Integer rating;
    private String content;
}
