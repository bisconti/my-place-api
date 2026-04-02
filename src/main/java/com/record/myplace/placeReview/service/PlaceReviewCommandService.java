package com.record.myplace.placeReview.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.record.myplace.placeReview.dto.PlaceReviewRequestDto;
import com.record.myplace.placeReview.dto.PlaceReviewResponseDto;

public interface PlaceReviewCommandService {

    PlaceReviewResponseDto createReview(PlaceReviewRequestDto requestDto, List<MultipartFile> images);

    void deleteReview(Long reviewId);
}