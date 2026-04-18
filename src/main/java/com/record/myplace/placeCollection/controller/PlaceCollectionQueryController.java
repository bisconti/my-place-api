package com.record.myplace.placeCollection.controller;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.placeCollection.dto.PlaceCollectionDetailResponse;
import com.record.myplace.placeCollection.dto.PlaceCollectionListResponse;
import com.record.myplace.placeCollection.service.PlaceCollectionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place-collections")
@Tag(name = "Place Collection Query", description = "저장 리스트 조회 API")
public class PlaceCollectionQueryController {

    private final PlaceCollectionQueryService placeCollectionQueryService;

    @Operation(summary = "내 저장 리스트 조회", description = "로그인 사용자의 저장 리스트를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<PlaceCollectionListResponse> getMyCollections(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "저장 여부를 함께 조회할 식당 ID", example = "82522548")
            @RequestParam(required = false) String placeId
    ) {
        PlaceCollectionListResponse response = placeCollectionQueryService.getMyCollections(user.getEmail(), placeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "저장 리스트 상세 조회", description = "저장 리스트 상세와 저장된 식당 목록을 조회합니다.")
    @GetMapping("/{collectionId}")
    public ResponseEntity<PlaceCollectionDetailResponse> getCollectionDetail(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long collectionId
    ) {
        PlaceCollectionDetailResponse response = placeCollectionQueryService.getCollectionDetail(user.getEmail(), collectionId);
        return ResponseEntity.ok(response);
    }
}
