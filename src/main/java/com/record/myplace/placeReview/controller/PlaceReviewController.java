package com.record.myplace.placeReview.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.record.myplace.placeReview.dto.PlaceReviewRequestDto;
import com.record.myplace.placeReview.dto.PlaceReviewResponseDto;
import com.record.myplace.placeReview.dto.PlaceReviewSummaryDto;
import com.record.myplace.placeReview.service.PlaceReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlaceReviewController {

    private final PlaceReviewService placeReviewService;
    private final ObjectMapper objectMapper;

    // 리뷰 등록
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createReview(
            @RequestPart("review") String reviewJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            PlaceReviewRequestDto requestDto = objectMapper.readValue(reviewJson, PlaceReviewRequestDto.class);
            PlaceReviewResponseDto responseDto = placeReviewService.createReview(requestDto, images);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorMap);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "리뷰 등록 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    // 장소별 리뷰 조회
    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<PlaceReviewResponseDto>> getReviewsByPlaceId(@PathVariable String placeId) {
        List<PlaceReviewResponseDto> reviewList = placeReviewService.getReviewsByPlaceId(placeId);
        return ResponseEntity.ok(reviewList);
    }

    // 사용자별 리뷰 조회
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<PlaceReviewResponseDto>> getReviewsByUserEmail(@PathVariable String userEmail) {
        List<PlaceReviewResponseDto> reviewList = placeReviewService.getReviewsByUserEmail(userEmail);
        return ResponseEntity.ok(reviewList);
    }
    
    // 식당 상세페이지 별점, 리뷰 수 조회
    @GetMapping("/place/{placeId}/summary")
    public ResponseEntity<PlaceReviewSummaryDto> getReviewSummaryByPlaceId(@PathVariable String placeId) {
        PlaceReviewSummaryDto summaryDto = placeReviewService.getReviewSummaryByPlaceId(placeId);
        return ResponseEntity.ok(summaryDto);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        try {
            placeReviewService.deleteReview(reviewId);

            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("message", "리뷰가 삭제되었습니다.");

            return ResponseEntity.ok(resultMap);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorMap);
        }
    }
}