package com.record.myplace.recentplace.repository;

import com.record.myplace.recentplace.entity.RecentPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecentPlaceRepository extends JpaRepository<RecentPlace, Long> {

    Optional<RecentPlace> findByUserEmailAndPlaceId(String userEmail, String placeId);
}