package com.record.myplace.placeLike.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.placeLike.dto.PlaceLikeListResponse;
import com.record.myplace.placeLike.service.PlaceLikeQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place-likes")
@Tag(name = "Place Like Query", description = "찜 조회 API")
public class PlaceLikeQueryController {

    private final PlaceLikeQueryService placeLikeQueryService;

    @Operation(summary = "내 찜 목록 조회", description = "로그인한 사용자의 찜 목록을 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<PlaceLikeListResponse> myLikes(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        String useremail = user.getEmail();
        PlaceLikeListResponse res = placeLikeQueryService.myLikes(useremail);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "찜 여부 조회", description = "특정 placeId의 찜 여부를 조회합니다.")
    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "장소 ID", example = "KAKAO_123")
            @RequestParam String placeId
    ) {
        String useremail = user.getEmail();
        boolean liked = placeLikeQueryService.exists(useremail, placeId);
        return ResponseEntity.ok(liked);
    }

    @Operation(summary = "내 찜 개수 조회", description = "로그인한 사용자의 찜 개수를 조회합니다.")
    @GetMapping("/count")
    public ResponseEntity<Long> count(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        String useremail = user.getEmail();
        long count = placeLikeQueryService.count(useremail);
        return ResponseEntity.ok(count);
    }
}