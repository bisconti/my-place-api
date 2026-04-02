package com.record.myplace.placeReview.service;

import java.util.List;

import com.record.myplace.placeReview.dto.PlaceReviewItemDto;
import com.record.myplace.placeReview.dto.PlaceReviewSummaryDto;

public interface PlaceReviewQueryService {

    List<PlaceReviewItemDto> getReviewsByUserEmail(String userEmail);

    List<PlaceReviewItemDto> getReviewsByPlaceId(String placeId);

    PlaceReviewSummaryDto getReviewSummaryByPlaceId(String placeId);

    long getReviewCountByUserEmail(String userEmail);
}