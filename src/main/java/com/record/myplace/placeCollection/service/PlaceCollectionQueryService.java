package com.record.myplace.placeCollection.service;

import com.record.myplace.placeCollection.dto.PlaceCollectionDetailResponse;
import com.record.myplace.placeCollection.dto.PlaceCollectionListResponse;

public interface PlaceCollectionQueryService {

    PlaceCollectionListResponse getMyCollections(String useremail, String placeId);

    PlaceCollectionDetailResponse getCollectionDetail(String useremail, Long collectionId);
}
