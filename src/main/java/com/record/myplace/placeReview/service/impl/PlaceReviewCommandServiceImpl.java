package com.record.myplace.placeReview.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.record.myplace.place.service.PlaceService;
import com.record.myplace.placeReview.dto.PlaceReviewRequestDto;
import com.record.myplace.placeReview.dto.PlaceReviewResponseDto;
import com.record.myplace.placeReview.entity.PlaceReview;
import com.record.myplace.placeReview.entity.PlaceReviewImage;
import com.record.myplace.placeReview.repository.PlaceReviewImageRepository;
import com.record.myplace.placeReview.repository.PlaceReviewRepository;
import com.record.myplace.placeReview.service.PlaceReviewCommandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceReviewCommandServiceImpl implements PlaceReviewCommandService {

    private final PlaceReviewRepository placeReviewRepository;
    private final PlaceReviewImageRepository placeReviewImageRepository;
    private final PlaceService placeService;
    
    // 파일 업로드 경로
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public PlaceReviewResponseDto createReview(PlaceReviewRequestDto requestDto, List<MultipartFile> images) {
        boolean exists = placeReviewRepository.existsByUserEmailAndPlaceId(
                requestDto.getUserEmail(),
                requestDto.getPlaceId()
        );

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
        review.setUserEmail(requestDto.getUserEmail());
        review.setPlaceId(requestDto.getPlaceId());
        review.setRating(requestDto.getRating());
        review.setContent(requestDto.getContent());

        PlaceReview saved = placeReviewRepository.save(review);
        
        // 이미지 저장 로직
        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile file = images.get(i);
                if (file.isEmpty()) continue;

                String originalFileName = file.getOriginalFilename();
                String storedFileName = UUID.randomUUID() + "_" + originalFileName;

                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                File dest = new File(uploadDir + "/" + storedFileName);
                
                try {
                    file.transferTo(dest); // 실제 파일 저장
                } catch (IOException e) {
                    throw new RuntimeException("파일 저장 실패: " + originalFileName, e);
                }

                // DB에 저장되는 경로 (프론트에서 접근할 상대 경로)
                String filePath = "/uploads/reviews/" + storedFileName;

                PlaceReviewImage image = new PlaceReviewImage();
                image.setReview(saved);
                image.setOriginalFileName(originalFileName);
                image.setStoredFileName(storedFileName);
                image.setFilePath(filePath);
                image.setFileSize(file.getSize());
                image.setSortOrder(i);

                placeReviewImageRepository.save(image);
            }
        }

        PlaceReviewResponseDto response = new PlaceReviewResponseDto();
        response.setId(saved.getId());
        response.setUserEmail(saved.getUserEmail());
        response.setPlaceId(saved.getPlaceId());
        response.setPlaceName(requestDto.getPlaceName());
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