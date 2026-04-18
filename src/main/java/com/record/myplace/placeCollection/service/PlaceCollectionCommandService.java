package com.record.myplace.placeCollection.service;

import com.record.myplace.placeCollection.dto.PlaceCollectionCreateRequest;
import com.record.myplace.placeCollection.dto.PlaceCollectionResponse;
import com.record.myplace.placeCollection.dto.PlaceCollectionSavePlaceRequest;
import com.record.myplace.placeCollection.dto.PlaceCollectionSavePlaceResponse;

public interface PlaceCollectionCommandService {

    PlaceCollectionResponse createCollection(String useremail, PlaceCollectionCreateRequest request);

    PlaceCollectionSavePlaceResponse savePlace(String useremail, Long collectionId, PlaceCollectionSavePlaceRequest request);

    void removePlace(String useremail, Long collectionId, String placeId);

    void deleteCollection(String useremail, Long collectionId);
}
