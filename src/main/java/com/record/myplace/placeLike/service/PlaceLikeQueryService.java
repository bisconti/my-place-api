package com.record.myplace.placeLike.service;

import com.record.myplace.placeLike.dto.PlaceLikeListResponse;

public interface PlaceLikeQueryService {

    PlaceLikeListResponse myLikes(String useremail);

    boolean exists(String useremail, String placeId);

    long count(String useremail);
}