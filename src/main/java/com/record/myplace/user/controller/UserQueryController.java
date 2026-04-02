package com.record.myplace.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.placeReview.service.PlaceReviewQueryService;
import com.record.myplace.user.dto.MeResponse;
import com.record.myplace.user.service.UserQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Query", description = "사용자 조회 API")
public class UserQueryController {

    private final UserQueryService userQueryService;
    private final PlaceReviewQueryService placeReviewQueryService;

    @Operation(summary = "내 프로필 조회", description = "로그인한 사용자의 프로필 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(userQueryService.getMe(user.getEmail()));
    }

    @Operation(summary = "사용자 리뷰 건수 조회", description = "사용자 이메일 기준 리뷰 작성 건수를 조회합니다.")
    @GetMapping("/{userEmail}/count")
    public ResponseEntity<Long> getReviewCountByUserEmail(
            @Parameter(description = "사용자 이메일", example = "test@example.com")
            @PathVariable String userEmail) {

        long count = placeReviewQueryService.getReviewCountByUserEmail(userEmail);
        return ResponseEntity.ok(count);
    }
}