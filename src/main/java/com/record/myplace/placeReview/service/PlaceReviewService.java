package com.record.myplace.placeReview.service;

import java.util.List;

import com.record.myplace.placeReview.dto.PlaceReviewRequestDto;
import com.record.myplace.placeReview.dto.PlaceReviewResponseDto;

public interface PlaceReviewService {

    PlaceReviewResponseDto createReview(PlaceReviewRequestDto requestDto);

    List<PlaceReviewResponseDto> getReviewsByPlaceId(String placeId);

    List<PlaceReviewResponseDto> getReviewsByUserEmail(String userEmail);

    void deleteReview(Long reviewId);
}
