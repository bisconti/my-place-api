package com.record.myplace.placeReview.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.record.myplace.auth.principal.CustomUserDetails;
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
@Tag(name = "Place Review Command", description = "리뷰 등록/수정/삭제 API")
public class PlaceReviewCommandController {

    private final PlaceReviewCommandService placeReviewCommandService;

    @Operation(summary = "리뷰 등록", description = "로그인한 사용자의 리뷰와 이미지 파일을 등록합니다.")
    @PostMapping
    public ResponseEntity<PlaceReviewResponseDto> createReview(
            @AuthenticationPrincipal CustomUserDetails user,
            @ModelAttribute PlaceReviewRequestDto requestDto,
            @Parameter(description = "리뷰 이미지 파일 목록")
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        return ResponseEntity.ok(placeReviewCommandService.createReview(user.getEmail(), requestDto, images));
    }

    @Operation(summary = "리뷰 수정", description = "로그인한 사용자가 작성한 리뷰의 별점과 내용을 수정합니다.")
    @PutMapping("/{reviewId}")
    public ResponseEntity<PlaceReviewResponseDto> updateReview(
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "리뷰 ID", example = "1")
            @PathVariable Long reviewId,
            @RequestBody PlaceReviewRequestDto requestDto) {

        return ResponseEntity.ok(placeReviewCommandService.updateReview(user.getEmail(), reviewId, requestDto));
    }

    @Operation(summary = "리뷰 삭제", description = "로그인한 사용자가 작성한 리뷰를 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "리뷰 ID", example = "1")
            @PathVariable Long reviewId) {

        placeReviewCommandService.deleteReview(user.getEmail(), reviewId);
        return ResponseEntity.noContent().build();
    }
}
