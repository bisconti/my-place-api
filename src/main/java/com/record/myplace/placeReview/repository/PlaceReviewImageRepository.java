package com.record.myplace.placeReview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.record.myplace.placeReview.entity.PlaceReviewImage;

public interface PlaceReviewImageRepository extends JpaRepository<PlaceReviewImage, Long> {
}
