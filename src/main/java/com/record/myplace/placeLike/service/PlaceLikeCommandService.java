package com.record.myplace.placeLike.service;

import com.record.myplace.placeLike.dto.PlaceLikeResponse;
import com.record.myplace.placeLike.dto.PlaceLikeToggleRequest;

public interface PlaceLikeCommandService {

    PlaceLikeResponse toggle(String useremail, PlaceLikeToggleRequest req);
}