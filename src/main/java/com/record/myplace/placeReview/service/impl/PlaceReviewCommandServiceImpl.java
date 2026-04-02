package com.record.myplace.placeReview.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.record.myplace.placeReview.dto.PlaceReviewRequestDto;
import com.record.myplace.placeReview.dto.PlaceReviewResponseDto;
import com.record.myplace.placeReview.entity.PlaceReview;
import com.record.myplace.placeReview.repository.PlaceReviewRepository;
import com.record.myplace.placeReview.service.PlaceReviewCommandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceReviewCommandServiceImpl implements PlaceReviewCommandService {

    private final PlaceReviewRepository placeReviewRepository;

    @Override
    public PlaceReviewResponseDto createReview(PlaceReviewRequestDto requestDto, List<MultipartFile> images) {
        boolean exists = placeReviewRepository.existsByUserEmailAndPlaceId(
                requestDto.getUserEmail(),
                requestDto.getPlaceId()
        );

        if (exists) {
            throw new IllegalArgumentException("이미 해당 장소에 작성한 리뷰가 있습니다.");
        }

        PlaceReview review = new PlaceReview();
        review.setUserEmail(requestDto.getUserEmail());
        review.setPlaceId(requestDto.getPlaceId());
        review.setRating(requestDto.getRating());
        review.setContent(requestDto.getContent());

        PlaceReview saved = placeReviewRepository.save(review);

        PlaceReviewResponseDto response = new PlaceReviewResponseDto();
        response.setId(saved.getId());
        response.setUserEmail(saved.getUserEmail());
        response.setPlaceId(saved.getPlaceId());
        response.setRating(saved.getRating());
        response.setContent(saved.getContent());
        response.setCreatedAt(saved.getCreatedAt());
        response.setUpdatedAt(saved.getUpdatedAt());
        response.setImages(Collections.emptyList());

        return response;
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (!placeReviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("리뷰가 존재하지 않습니다.");
        }

        placeReviewRepository.deleteById(reviewId);
    }
}