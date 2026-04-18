package com.record.myplace.placeCollection.repository;

import com.record.myplace.placeCollection.entity.PlaceCollectionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceCollectionItemRepository extends JpaRepository<PlaceCollectionItem, Long> {

    Optional<PlaceCollectionItem> findByCollectionIdAndPlaceId(Long collectionId, String placeId);

    boolean existsByCollectionIdAndPlaceId(Long collectionId, String placeId);

    long deleteByCollectionIdAndPlaceId(Long collectionId, String placeId);

    long deleteByCollectionId(Long collectionId);
}
