package com.record.myplace.placeReview.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.record.myplace.placeReview.dto.PlaceReviewRequestDto;
import com.record.myplace.placeReview.dto.PlaceReviewResponseDto;

public interface PlaceReviewCommandService {

    PlaceReviewResponseDto createReview(String userEmail, PlaceReviewRequestDto requestDto, List<MultipartFile> images);

    PlaceReviewResponseDto updateReview(String userEmail, Long reviewId, PlaceReviewRequestDto requestDto);

    void deleteReview(String userEmail, Long reviewId);
}
