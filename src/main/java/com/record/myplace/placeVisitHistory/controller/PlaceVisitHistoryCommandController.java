package com.record.myplace.placeVisitHistory.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.placeVisitHistory.dto.PlaceVisitHistoryCreateRequestDto;
import com.record.myplace.placeVisitHistory.service.PlaceVisitHistoryCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/place-visit-histories")
@RequiredArgsConstructor
@Tag(name = "Place Visit History Command", description = "방문 기록 등록 API")
public class PlaceVisitHistoryCommandController {

    private final PlaceVisitHistoryCommandService placeVisitHistoryCommandService;

    @Operation(summary = "가봤어요 방문 기록 등록")
    @PostMapping
    public void createVisitHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PlaceVisitHistoryCreateRequestDto requestDto
    ) {
        placeVisitHistoryCommandService.createVisitHistory(userDetails.getEmail(), requestDto);
    }
}