package com.record.myplace.placeReview.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.placeReview.dto.PlaceReviewImageResponseDto;
import com.record.myplace.placeReview.dto.PlaceReviewItemDto;
import com.record.myplace.placeReview.dto.PlaceReviewSummaryDto;
import com.record.myplace.placeReview.mapper.PlaceReviewQueryMapper;
import com.record.myplace.placeReview.repository.PlaceReviewRepository;
import com.record.myplace.placeReview.service.PlaceReviewQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceReviewQueryServiceImpl implements PlaceReviewQueryService {

    private final PlaceReviewQueryMapper placeReviewQueryMapper;
    private final PlaceReviewRepository placeReviewRepository;

    @Override
    public List<PlaceReviewItemDto> getReviewsByUserEmail(String userEmail) {
        List<PlaceReviewItemDto> reviews = placeReviewQueryMapper.selectMyReviews(userEmail);
        attachImages(reviews);
        return reviews;
    }

    @Override
    public List<PlaceReviewItemDto> getReviewsByPlaceId(String placeId) {
        List<PlaceReviewItemDto> reviews = placeReviewQueryMapper.selectReviewsByPlaceId(placeId);
        attachImages(reviews);
        return reviews;
    }

    @Override
    public PlaceReviewSummaryDto getReviewSummaryByPlaceId(String placeId) {
        PlaceReviewSummaryDto summary = placeReviewQueryMapper.selectReviewSummary(placeId);

        if (summary == null) {
            summary = new PlaceReviewSummaryDto();
            summary.setPlaceId(placeId);
            summary.setAverageRating(0.0);
            summary.setReviewCount(0L);
        }

        return summary;
    }

    @Override
    public long getReviewCountByUserEmail(String userEmail) {
        return placeReviewRepository.countByUserEmail(userEmail);
    }

    private void attachImages(List<PlaceReviewItemDto> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return;
        }

        List<Long> reviewIds = reviews.stream()
                .map(PlaceReviewItemDto::getId)
                .toList();

        List<PlaceReviewImageResponseDto> imageList =
                placeReviewQueryMapper.selectReviewImagesByReviewIds(reviewIds);

        Map<Long, List<PlaceReviewImageResponseDto>> imageMap = imageList.stream()
                .collect(Collectors.groupingBy(PlaceReviewImageResponseDto::getReviewId));

        for (PlaceReviewItemDto review : reviews) {
            review.setImages(imageMap.getOrDefault(review.getId(), Collections.emptyList()));
        }
    }
}