package com.record.myplace.placeReview.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.placeReview.dto.PlaceReviewRequestDto;
import com.record.myplace.placeReview.dto.PlaceReviewResponseDto;
import com.record.myplace.placeReview.dto.PlaceReviewSummaryDto;
import com.record.myplace.placeReview.entity.PlaceReview;
import com.record.myplace.placeReview.repository.PlaceReviewRepository;
import com.record.myplace.placeReview.service.PlaceReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceReviewServiceImpl implements PlaceReviewService {

    private final PlaceReviewRepository placeReviewRepository;

    @Override
    public PlaceReviewResponseDto createReview(PlaceReviewRequestDto
    		requestDto) {

        validateRequest(requestDto);

        // 한 유저가 같은 장소에 리뷰 1개만 허용하고 싶을 때
        if (placeReviewRepository.existsByUserEmailAndPlaceId(requestDto.getUserEmail(), requestDto.getPlaceId())) {
            throw new IllegalArgumentException("이미 해당 장소에 작성한 리뷰가 있습니다.");
        }

        PlaceReview review = new PlaceReview();
        review.setUserEmail(requestDto.getUserEmail());
        review.setPlaceId(requestDto.getPlaceId());
        review.setRating(requestDto.getRating());
        review.setContent(requestDto.getContent().trim());

        PlaceReview savedReview = placeReviewRepository.save(review);

        return PlaceReviewResponseDto.fromEntity(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceReviewResponseDto> getReviewsByPlaceId(String placeId) {
        return placeReviewRepository.findByPlaceIdOrderByCreatedAtDesc(placeId)
                .stream()
                .map(PlaceReviewResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceReviewResponseDto> getReviewsByUserEmail(String userEmail) {
        return placeReviewRepository.findByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(PlaceReviewResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // 식당 상세페이지 진입 시 식당 별점과 리뷰 수를 구하기
    @Override
    @Transactional(readOnly = true)
    public PlaceReviewSummaryDto getReviewSummaryByPlaceId(String placeId) {
        long reviewCount = placeReviewRepository.countByPlaceId(placeId);
        Double averageRating = placeReviewRepository.findAverageRatingByPlaceId(placeId);

        if (averageRating == null) {
            averageRating = 0.0;
        }

        return PlaceReviewSummaryDto.builder()
                .placeId(placeId)
                .averageRating(Math.round(averageRating * 10) / 10.0) // 소수점 1자리
                .reviewCount(reviewCount)
                .build();
    }
    
    // 마이 페이지 내 리뷰건수 조회
    @Override
    @Transactional(readOnly = true)
    public long getReviewCountByUserEmail(String userEmail) {
        return placeReviewRepository.countByUserEmail(userEmail);
    }

    @Override
    public void deleteReview(Long reviewId) {
        PlaceReview review = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        placeReviewRepository.delete(review);
    }

    private void validateRequest(PlaceReviewRequestDto requestDto) {
        if (requestDto.getUserEmail() == null || requestDto.getUserEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("사용자 이메일은 필수입니다.");
        }

        if (requestDto.getPlaceId() == null || requestDto.getPlaceId().trim().isEmpty()) {
            throw new IllegalArgumentException("장소 ID는 필수입니다.");
        }

        if (requestDto.getRating() == null || requestDto.getRating() < 1 || requestDto.getRating() > 5) {
            throw new IllegalArgumentException("별점은 1점부터 5점까지 입력해야 합니다.");
        }

        if (requestDto.getContent() == null || requestDto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("리뷰 내용은 필수입니다.");
        }

        if (requestDto.getContent().trim().length() > 1000) {
            throw new IllegalArgumentException("리뷰 내용은 1000자 이하로 입력해주세요.");
        }
    }
}
