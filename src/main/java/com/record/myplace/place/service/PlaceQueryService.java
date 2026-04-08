package com.record.myplace.place.service;

import com.record.myplace.place.dto.PlaceDetailResponseDto;

public interface PlaceQueryService {

    PlaceDetailResponseDto getPlaceDetail(String placeId, String userEmail);
}