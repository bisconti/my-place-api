package com.record.myplace.placeReview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.record.myplace.placeReview.entity.PlaceReview;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {

    boolean existsByUserEmailAndPlaceId(String userEmail, String placeId);

    long countByPlaceId(String placeId);

    long countByUserEmail(String userEmail);
}