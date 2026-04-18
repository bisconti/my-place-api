package com.record.myplace.placeCollection.controller;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.placeCollection.dto.PlaceCollectionCreateRequest;
import com.record.myplace.placeCollection.dto.PlaceCollectionResponse;
import com.record.myplace.placeCollection.dto.PlaceCollectionSavePlaceRequest;
import com.record.myplace.placeCollection.dto.PlaceCollectionSavePlaceResponse;
import com.record.myplace.placeCollection.service.PlaceCollectionCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place-collections")
@Tag(name = "Place Collection Command", description = "저장 리스트 생성 및 저장 API")
public class PlaceCollectionCommandController {

    private final PlaceCollectionCommandService placeCollectionCommandService;

    @Operation(summary = "저장 리스트 생성", description = "로그인 사용자의 저장 리스트를 생성합니다.")
    @PostMapping
    public ResponseEntity<PlaceCollectionResponse> createCollection(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody PlaceCollectionCreateRequest request
    ) {
        PlaceCollectionResponse response = placeCollectionCommandService.createCollection(user.getEmail(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "저장 리스트에 식당 저장", description = "선택한 저장 리스트에 식당을 추가합니다.")
    @PostMapping("/{collectionId}/places")
    public ResponseEntity<PlaceCollectionSavePlaceResponse> savePlace(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long collectionId,
            @RequestBody PlaceCollectionSavePlaceRequest request
    ) {
        PlaceCollectionSavePlaceResponse response = placeCollectionCommandService.savePlace(user.getEmail(), collectionId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "저장 리스트에서 식당 제거", description = "선택한 저장 리스트에서 식당을 제거합니다.")
    @DeleteMapping("/{collectionId}/places/{placeId}")
    public ResponseEntity<Void> removePlace(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long collectionId,
            @PathVariable String placeId
    ) {
        placeCollectionCommandService.removePlace(user.getEmail(), collectionId, placeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "저장 리스트 삭제", description = "저장 리스트와 포함된 식당을 함께 삭제합니다.")
    @DeleteMapping("/{collectionId}")
    public ResponseEntity<Void> deleteCollection(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long collectionId
    ) {
        placeCollectionCommandService.deleteCollection(user.getEmail(), collectionId);
        return ResponseEntity.noContent().build();
    }
}
