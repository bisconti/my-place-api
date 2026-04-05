package com.record.myplace.placeVisitHistory.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.record.myplace.placeVisitHistory.entity.PlaceVisitHistory;

public interface PlaceVisitHistoryRepository extends JpaRepository<PlaceVisitHistory, Long> {

    boolean existsByUserEmailAndPlaceId(String userEmail, String placeId);

    boolean existsByUserEmailAndPlaceIdAndVisitDate(String userEmail, String placeId, LocalDate visitDate);

    Optional<PlaceVisitHistory> findByUserEmailAndPlaceIdAndVisitDate(String userEmail, String placeId, LocalDate visitDate);
}