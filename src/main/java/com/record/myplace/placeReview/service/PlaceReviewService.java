package com.record.myplace.placeReview.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.record.myplace.placeReview.dto.PlaceReviewRequestDto;
import com.record.myplace.placeReview.dto.PlaceReviewResponseDto;
import com.record.myplace.placeReview.dto.PlaceReviewSummaryDto;

public interface PlaceReviewService {

    PlaceReviewResponseDto createReview(PlaceReviewRequestDto requestDto, List<MultipartFile> images);

    List<PlaceReviewResponseDto> getReviewsByPlaceId(String placeId);

    List<PlaceReviewResponseDto> getReviewsByUserEmail(String userEmail);
    
    PlaceReviewSummaryDto getReviewSummaryByPlaceId(String placeId);
    
    long getReviewCountByUserEmail(String userEmail);

    void deleteReview(Long reviewId);
}
