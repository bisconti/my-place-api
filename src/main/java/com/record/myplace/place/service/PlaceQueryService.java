package com.record.myplace.place.service;

import java.util.List;

import com.record.myplace.place.dto.PlaceAutoCompleteResponseDto;
import com.record.myplace.place.dto.PlaceDetailResponseDto;
import com.record.myplace.place.dto.PlaceListItemResponseDto;

public interface PlaceQueryService {

    PlaceDetailResponseDto getPlaceDetail(String placeId, String userEmail);
    
    List<PlaceAutoCompleteResponseDto> getPlaceAutoCompleteList(String keyword);

    List<PlaceListItemResponseDto> getPlaceListMetadata(List<String> placeIds, String userEmail);
}
