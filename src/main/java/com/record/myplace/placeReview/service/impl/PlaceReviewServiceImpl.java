package com.record.myplace.placeReview.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.record.myplace.placeReview.dto.PlaceReviewRequestDto;
import com.record.myplace.placeReview.dto.PlaceReviewResponseDto;
import com.record.myplace.placeReview.dto.PlaceReviewSummaryDto;
import com.record.myplace.placeReview.entity.PlaceReview;
import com.record.myplace.placeReview.entity.PlaceReviewImage;
import com.record.myplace.placeReview.repository.PlaceReviewRepository;
import com.record.myplace.placeReview.service.PlaceReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceReviewServiceImpl implements PlaceReviewService {
	
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final PlaceReviewRepository placeReviewRepository;
    
	@Override
	public PlaceReviewResponseDto createReview(PlaceReviewRequestDto requestDto, List<MultipartFile> images) {
        validateRequest(requestDto);
        validateImages(images);

        PlaceReview review = new PlaceReview();
        review.setUserEmail(requestDto.getUserEmail());
        review.setPlaceId(requestDto.getPlaceId());
        review.setRating(requestDto.getRating());
        review.setContent(requestDto.getContent().trim());

        PlaceReview savedReview = placeReviewRepository.save(review);

        if (images != null && !images.isEmpty()) {
            int sortOrder = 0;

            for (MultipartFile image : images) {
                if (image == null || image.isEmpty()) {
                    continue;
                }

                String originalFileName = image.getOriginalFilename();
                String extension = extractExtension(originalFileName);
                String storedFileName = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);

                Path saveDir = Paths.get(uploadDir);
                Path savePath = saveDir.resolve(storedFileName);

                try {
                    if (!Files.exists(saveDir)) {
                        Files.createDirectories(saveDir);
                    }

                    Files.copy(image.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
                }

                PlaceReviewImage reviewImage = new PlaceReviewImage();
                reviewImage.setReview(savedReview);
                reviewImage.setOriginalFileName(originalFileName);
                reviewImage.setStoredFileName(storedFileName);
                reviewImage.setFilePath("/uploads/reviews/" + storedFileName);
                reviewImage.setFileSize(image.getSize());
                reviewImage.setSortOrder(sortOrder++);

                savedReview.getImages().add(reviewImage);
            }
        }

        return PlaceReviewResponseDto.fromEntity(savedReview);
	}

    @Override
    @Transactional(readOnly = true)
    public List<PlaceReviewResponseDto> getReviewsByPlaceId(String placeId) {
        return placeReviewRepository.findByPlaceIdWithUser(placeId)
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
    

    private void validateImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        if (images.size() > 10) {
            throw new IllegalArgumentException("이미지는 최대 10장까지 업로드할 수 있습니다.");
        }

        for (MultipartFile image : images) {
            if (image == null || image.isEmpty()) {
                continue;
            }

            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
            }

            if (image.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("각 이미지 파일은 10MB 이하만 가능합니다.");
            }
        }
    }
    
    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
