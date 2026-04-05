package com.record.myplace.placeVisitHistory.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.placeVisitHistory.dto.PlaceVisitHistoryResponseDto;
import com.record.myplace.placeVisitHistory.dto.PlaceVisitedResponseDto;
import com.record.myplace.placeVisitHistory.mapper.PlaceVisitHistoryQueryMapper;
import com.record.myplace.placeVisitHistory.service.PlaceVisitHistoryQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceVisitHistoryQueryServiceImpl implements PlaceVisitHistoryQueryService {

    private final PlaceVisitHistoryQueryMapper placeVisitHistoryQueryMapper;

    @Override
    public List<PlaceVisitHistoryResponseDto> getVisitHistories(String userEmail) {
        return placeVisitHistoryQueryMapper.selectVisitHistoriesByUserEmail(userEmail);
    }

    @Override
    public PlaceVisitedResponseDto getVisited(String userEmail, String placeId) {
        return placeVisitHistoryQueryMapper.selectVisitedByUserEmailAndPlaceId(userEmail, placeId);
    }
}