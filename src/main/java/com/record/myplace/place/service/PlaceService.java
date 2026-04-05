package com.record.myplace.place.service;

public interface PlaceService {

    void ensurePlaceExists(
            String placeId,
            String placeName,
            String address,
            String roadAddress,
            String category,
            String phone
    );
}