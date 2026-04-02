package com.record.myplace.placeLike.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.record.myplace.placeLike.entity.PlaceLike;

public interface PlaceLikeRepository extends JpaRepository<PlaceLike, Long> {

    boolean existsByUseremailAndPlaceId(String useremail, String placeId);

    Optional<PlaceLike> findByUseremailAndPlaceId(String useremail, String placeId);

    long deleteByUseremailAndPlaceId(String useremail, String placeId);

	long countByUseremail(String useremail);
}
