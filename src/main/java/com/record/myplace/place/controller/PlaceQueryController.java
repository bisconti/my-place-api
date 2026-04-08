package com.record.myplace.place.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.place.dto.PlaceDetailResponseDto;
import com.record.myplace.place.service.PlaceQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
@Tag(name = "Place Query", description = "식당 조회 API")
@Slf4j
public class PlaceQueryController {

    private final PlaceQueryService placeQueryService;

    @Operation(summary = "식당 상세 조회", description = "placeId 기준으로 식당 상세 정보를 조회합니다.")
    @GetMapping("/{placeId}")
    public PlaceDetailResponseDto getPlaceDetail(
            @PathVariable String placeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String userEmail = userDetails != null ? userDetails.getEmail() : null;
        
        log.info("place detail 조회: placeId={}, userEmail={}", placeId, userEmail);
        return placeQueryService.getPlaceDetail(placeId, userEmail);
    }
}