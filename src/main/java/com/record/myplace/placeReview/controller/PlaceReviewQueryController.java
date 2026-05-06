package com.record.myplace.placeReview.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.placeReview.dto.PlaceReviewItemDto;
import com.record.myplace.placeReview.dto.PlaceReviewSummaryDto;
import com.record.myplace.placeReview.service.PlaceReviewQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Place Review Query", description = "리뷰 조회 API")
public class PlaceReviewQueryController {

    private final PlaceReviewQueryService placeReviewQueryService;

    @Operation(summary = "내 리뷰 목록 조회", description = "로그인한 사용자가 작성한 리뷰 목록을 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<List<PlaceReviewItemDto>> getMyReviews(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(placeReviewQueryService.getReviewsByUserEmail(user.getEmail()));
    }

    @Operation(summary = "장소별 리뷰 목록 조회", description = "장소 ID 기준으로 리뷰 목록을 조회합니다.")
    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<PlaceReviewItemDto>> getReviewsByPlace(
            @Parameter(description = "장소 ID", example = "PLACE_001")
            @PathVariable String placeId) {

        return ResponseEntity.ok(placeReviewQueryService.getReviewsByPlaceId(placeId));
    }

    @Operation(summary = "장소별 리뷰 요약 조회", description = "장소 ID 기준으로 평균 별점과 리뷰 개수를 조회합니다.")
    @GetMapping("/summary/{placeId}")
    public ResponseEntity<PlaceReviewSummaryDto> getSummary(
            @Parameter(description = "장소 ID", example = "PLACE_001")
            @PathVariable String placeId) {

        return ResponseEntity.ok(placeReviewQueryService.getReviewSummaryByPlaceId(placeId));
    }
}
