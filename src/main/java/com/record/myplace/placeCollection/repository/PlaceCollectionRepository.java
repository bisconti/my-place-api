package com.record.myplace.placeCollection.repository;

import com.record.myplace.placeCollection.entity.PlaceCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceCollectionRepository extends JpaRepository<PlaceCollection, Long> {

    boolean existsByUseremailAndName(String useremail, String name);

    Optional<PlaceCollection> findByUseremailAndName(String useremail, String name);

    Optional<PlaceCollection> findByIdAndUseremail(Long id, String useremail);
}
