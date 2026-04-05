package com.record.myplace.placeReview.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.record.myplace.placeReview.dto.PlaceReviewRequestDto;
import com.record.myplace.placeReview.dto.PlaceReviewResponseDto;
import com.record.myplace.placeReview.service.PlaceReviewCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Place Review Command", description = "리뷰 등록/삭제 API")
public class PlaceReviewCommandController {

    private final PlaceReviewCommandService placeReviewCommandService;

    @Operation(summary = "리뷰 등록", description = "리뷰와 이미지 파일을 등록합니다.")
    @PostMapping
    public ResponseEntity<PlaceReviewResponseDto> createReview(
            @ModelAttribute PlaceReviewRequestDto requestDto,
            @Parameter(description = "리뷰 이미지 파일 목록")
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
    	
        System.out.println("받은 이미지 수: " + (images != null ? images.size() : 0));
        if (images != null) {
            images.forEach(img -> System.out.println("파일명: " + img.getOriginalFilename() + ", 크기: " + img.getSize()));
        }

        return ResponseEntity.ok(placeReviewCommandService.createReview(requestDto, images));
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 ID 기준으로 리뷰를 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "리뷰 ID", example = "1")
            @PathVariable Long reviewId) {

        placeReviewCommandService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}