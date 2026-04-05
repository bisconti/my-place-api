package com.record.myplace.placeVisitHistory.service;

import com.record.myplace.placeVisitHistory.dto.PlaceVisitHistoryCreateRequestDto;

public interface PlaceVisitHistoryCommandService {

    void createVisitHistory(String userEmail, PlaceVisitHistoryCreateRequestDto requestDto);

    void createVisitHistoryFromReviewIfNotExists(String userEmail, String placeId);
}