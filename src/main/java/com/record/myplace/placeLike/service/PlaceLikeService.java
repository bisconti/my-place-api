package com.record.myplace.placeLike.service;

import com.record.myplace.placeLike.dto.PlaceLikeListResponse;
import com.record.myplace.placeLike.dto.PlaceLikeResponse;
import com.record.myplace.placeLike.dto.PlaceLikeToggleRequest;

public interface PlaceLikeService {
    PlaceLikeResponse toggle(String useremail, PlaceLikeToggleRequest req);
    PlaceLikeListResponse myLikes(String useremail);
    boolean exists(String useremail, String placeId);
}
