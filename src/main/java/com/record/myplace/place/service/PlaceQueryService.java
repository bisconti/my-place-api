package com.record.myplace.place.service;

import java.util.List;

import com.record.myplace.place.dto.PlaceAutoCompleteResponseDto;
import com.record.myplace.place.dto.PlaceDetailResponseDto;

public interface PlaceQueryService {

    PlaceDetailResponseDto getPlaceDetail(String placeId, String userEmail);
    
    List<PlaceAutoCompleteResponseDto> getPlaceAutoCompleteList(String keyword);
}