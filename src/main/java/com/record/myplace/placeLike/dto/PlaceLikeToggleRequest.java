package com.record.myplace.placeLike.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceLikeToggleRequest {
    private String placeId;
    private String placeName;
    private String address;
    private String category;
    private Boolean liked; // true면 찜, false면 해제
}
