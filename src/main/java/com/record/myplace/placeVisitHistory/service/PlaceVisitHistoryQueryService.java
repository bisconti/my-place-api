package com.record.myplace.placeVisitHistory.service;

import java.util.List;

import com.record.myplace.placeVisitHistory.dto.PlaceVisitHistoryResponseDto;
import com.record.myplace.placeVisitHistory.dto.PlaceVisitedResponseDto;

public interface PlaceVisitHistoryQueryService {

    List<PlaceVisitHistoryResponseDto> getVisitHistories(String userEmail);

    PlaceVisitedResponseDto getVisited(String userEmail, String placeId);
}