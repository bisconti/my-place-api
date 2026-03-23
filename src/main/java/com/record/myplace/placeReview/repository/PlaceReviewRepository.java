package com.record.myplace.placeReview.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.record.myplace.placeReview.entity.PlaceReview;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {

    List<PlaceReview> findByPlaceIdOrderByCreatedAtDesc(String placeId);

    List<PlaceReview> findByUserEmailOrderByCreatedAtDesc(String userEmail);

    Optional<PlaceReview> findByUserEmailAndPlaceId(String userEmail, String placeId);

    boolean existsByUserEmailAndPlaceId(String userEmail, String placeId);
    
    long countByPlaceId(String placeId);
    
    long countByUserEmail(String userEmail);

    @Query("SELECT AVG(pr.rating) FROM PlaceReview pr WHERE pr.placeId = :placeId")
    Double findAverageRatingByPlaceId(String placeId);
    
    // 식당 상세 페이지에서 리뷰 작성 이메일 - 유저 닉네임 매칭
    @Query("SELECT pr FROM PlaceReview pr JOIN FETCH pr.user WHERE pr.placeId = :placeId ORDER BY pr.createdAt DESC")
    List<PlaceReview> findByPlaceIdWithUser(String placeId);
}
