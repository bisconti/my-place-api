package com.record.myplace.placeLike.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceLikeResponse {
    private String placeId;
    private String placeName;
    private String address;
    private String category;
    private LocalDateTime createdAt;
    private boolean liked;
}
