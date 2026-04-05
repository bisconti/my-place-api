package com.record.myplace.placeVisitHistory.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.placeVisitHistory.dto.PlaceVisitHistoryCreateRequestDto;
import com.record.myplace.placeVisitHistory.entity.PlaceVisitHistory;
import com.record.myplace.placeVisitHistory.repository.PlaceVisitHistoryRepository;
import com.record.myplace.placeVisitHistory.service.PlaceVisitHistoryCommandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceVisitHistoryCommandServiceImpl implements PlaceVisitHistoryCommandService {

    private final PlaceVisitHistoryRepository placeVisitHistoryRepository;

    @Override
    public void createVisitHistory(String userEmail, PlaceVisitHistoryCreateRequestDto requestDto) {
        LocalDate visitDate = requestDto.getVisitDate() != null && !requestDto.getVisitDate().isBlank()
                ? LocalDate.parse(requestDto.getVisitDate())
                : LocalDate.now();

        boolean exists = placeVisitHistoryRepository.existsByUserEmailAndPlaceIdAndVisitDate(
                userEmail,
                requestDto.getPlaceId(),
                visitDate
        );

        if (exists) {
            return;
        }

        PlaceVisitHistory entity = new PlaceVisitHistory();
        entity.setUserEmail(userEmail);
        entity.setPlaceId(requestDto.getPlaceId());
        entity.setVisitDate(visitDate);
        entity.setVisitSource("MANUAL");

        placeVisitHistoryRepository.save(entity);
    }

    @Override
    public void createVisitHistoryFromReviewIfNotExists(String userEmail, String placeId) {
        LocalDate today = LocalDate.now();

        boolean exists = placeVisitHistoryRepository.existsByUserEmailAndPlaceId(userEmail, placeId);

        if (exists) {
            return;
        }

        PlaceVisitHistory entity = new PlaceVisitHistory();
        entity.setUserEmail(userEmail);
        entity.setPlaceId(placeId);
        entity.setVisitDate(today);
        entity.setVisitSource("REVIEW");

        placeVisitHistoryRepository.save(entity);
    }
}