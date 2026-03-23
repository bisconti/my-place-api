package com.record.myplace.placeReview.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.record.myplace.placeReview.entity.PlaceReviewImage;

public interface PlaceReviewImageRepository extends JpaRepository<PlaceReviewImage, Long> {

    List<PlaceReviewImage> findByReviewIdOrderBySortOrderAsc(Long reviewId);
}
