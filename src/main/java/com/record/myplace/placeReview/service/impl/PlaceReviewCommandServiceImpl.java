package com.record.myplace.placeReview.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.record.myplace.place.service.PlaceCommandService;
import com.record.myplace.placeReview.dto.PlaceReviewRequestDto;
import com.record.myplace.placeReview.dto.PlaceReviewResponseDto;
import com.record.myplace.placeReview.entity.PlaceReview;
import com.record.myplace.placeReview.entity.PlaceReviewImage;
import com.record.myplace.placeReview.repository.PlaceReviewImageRepository;
import com.record.myplace.placeReview.repository.PlaceReviewRepository;
import com.record.myplace.placeReview.service.PlaceReviewCommandService;
import com.record.myplace.placeVisitHistory.service.PlaceVisitHistoryCommandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceReviewCommandServiceImpl implements PlaceReviewCommandService {

    private final PlaceReviewRepository placeReviewRepository;
    private final PlaceReviewImageRepository placeReviewImageRepository;
    private final PlaceVisitHistoryCommandService placeVisitHistoryCommandService;
    private final PlaceCommandService placeService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public PlaceReviewResponseDto createReview(String userEmail, PlaceReviewRequestDto requestDto, List<MultipartFile> images) {
        validateReview(requestDto);

        boolean exists = placeReviewRepository.existsByUserEmailAndPlaceId(userEmail, requestDto.getPlaceId());
        if (exists) {
            throw new IllegalArgumentException("이미 해당 장소에 작성한 리뷰가 있습니다.");
        }

        placeService.ensurePlaceExists(
                requestDto.getPlaceId(),
                requestDto.getPlaceName(),
                requestDto.getAddress(),
                requestDto.getRoadAddress(),
                requestDto.getCategory(),
                requestDto.getPhone()
        );

        PlaceReview review = new PlaceReview();
        review.setUserEmail(userEmail);
        review.setPlaceId(requestDto.getPlaceId());
        review.setRating(requestDto.getRating());
        review.setContent(requestDto.getContent().trim());

        PlaceReview saved = placeReviewRepository.save(review);
        saveImages(saved, images);

        placeVisitHistoryCommandService.createVisitHistoryFromReviewIfNotExists(userEmail, requestDto.getPlaceId());

        PlaceReviewResponseDto response = toResponse(saved);
        response.setPlaceName(requestDto.getPlaceName());
        return response;
    }

    @Override
    public PlaceReviewResponseDto updateReview(String userEmail, Long reviewId, PlaceReviewRequestDto requestDto) {
        validateReviewContent(requestDto);

        PlaceReview review = placeReviewRepository.findByIdAndUserEmail(reviewId, userEmail)
                .orElseThrow(() -> new IllegalArgumentException("수정할 리뷰를 찾을 수 없습니다."));

        review.setRating(requestDto.getRating());
        review.setContent(requestDto.getContent().trim());

        return toResponse(review);
    }

    @Override
    public void deleteReview(String userEmail, Long reviewId) {
        PlaceReview review = placeReviewRepository.findByIdAndUserEmail(reviewId, userEmail)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 리뷰를 찾을 수 없습니다."));

        placeReviewRepository.delete(review);
    }

    private void validateReview(PlaceReviewRequestDto requestDto) {
        if (requestDto == null || !StringUtils.hasText(requestDto.getPlaceId())) {
            throw new IllegalArgumentException("장소 정보가 필요합니다.");
        }

        validateReviewContent(requestDto);
    }

    private void validateReviewContent(PlaceReviewRequestDto requestDto) {
        if (requestDto == null || requestDto.getRating() == null || requestDto.getRating() < 1 || requestDto.getRating() > 5) {
            throw new IllegalArgumentException("별점은 1점부터 5점까지 선택할 수 있습니다.");
        }

        if (!StringUtils.hasText(requestDto.getContent())) {
            throw new IllegalArgumentException("리뷰 내용을 입력해주세요.");
        }
    }

    private void saveImages(PlaceReview review, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
            throw new RuntimeException("업로드 폴더를 생성하지 못했습니다.");
        }

        for (int i = 0; i < images.size(); i++) {
            MultipartFile file = images.get(i);
            if (file == null || file.isEmpty()) {
                continue;
            }

            String originalFileName = file.getOriginalFilename();
            String storedFileName = UUID.randomUUID() + "_" + (originalFileName == null ? "review-image" : originalFileName);
            File dest = new File(uploadDir + "/" + storedFileName);

            try {
                file.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패: " + originalFileName, e);
            }

            PlaceReviewImage image = new PlaceReviewImage();
            image.setReview(review);
            image.setOriginalFileName(originalFileName);
            image.setStoredFileName(storedFileName);
            image.setFilePath("/uploads/reviews/" + storedFileName);
            image.setFileSize(file.getSize());
            image.setSortOrder(i);

            placeReviewImageRepository.save(image);
        }
    }

    private PlaceReviewResponseDto toResponse(PlaceReview review) {
        PlaceReviewResponseDto response = new PlaceReviewResponseDto();
        response.setId(review.getId());
        response.setUserEmail(review.getUserEmail());
        response.setPlaceId(review.getPlaceId());
        response.setPlaceName(review.getPlace() != null ? review.getPlace().getPlaceName() : null);
        response.setRating(review.getRating());
        response.setContent(review.getContent());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());
        response.setImages(Collections.emptyList());
        return response;
    }
}
