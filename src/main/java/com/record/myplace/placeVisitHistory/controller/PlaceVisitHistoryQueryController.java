package com.record.myplace.placeVisitHistory.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.placeVisitHistory.dto.PlaceVisitHistoryResponseDto;
import com.record.myplace.placeVisitHistory.dto.PlaceVisitedResponseDto;
import com.record.myplace.placeVisitHistory.service.PlaceVisitHistoryQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/place-visit-histories")
@RequiredArgsConstructor
@Tag(name = "Place Visit History Query", description = "방문 기록 조회 API")
public class PlaceVisitHistoryQueryController {

    private final PlaceVisitHistoryQueryService placeVisitHistoryQueryService;

    @Operation(summary = "내 방문 기록 목록 조회")
    @GetMapping
    public List<PlaceVisitHistoryResponseDto> getVisitHistories(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return placeVisitHistoryQueryService.getVisitHistories(userDetails.getEmail());
    }

    @Operation(summary = "특정 식당 방문 여부 조회")
    @GetMapping("/visited")
    public PlaceVisitedResponseDto getVisited(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String placeId
    ) {
        return placeVisitHistoryQueryService.getVisited(userDetails.getEmail(), placeId);
    }
}