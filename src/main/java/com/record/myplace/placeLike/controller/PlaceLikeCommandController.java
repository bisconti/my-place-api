package com.record.myplace.placeLike.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.placeLike.dto.PlaceLikeResponse;
import com.record.myplace.placeLike.dto.PlaceLikeToggleRequest;
import com.record.myplace.placeLike.service.PlaceLikeCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place-likes")
@Tag(name = "Place Like Command", description = "찜 변경 API")
public class PlaceLikeCommandController {

    private final PlaceLikeCommandService placeLikeCommandService;

    @Operation(summary = "찜 토글", description = "liked=true면 저장, liked=false면 삭제합니다.")
    @PostMapping("/toggle")
    public ResponseEntity<PlaceLikeResponse> toggle(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody PlaceLikeToggleRequest req
    ) {
        String useremail = user.getEmail();
        PlaceLikeResponse res = placeLikeCommandService.toggle(useremail, req);
        return ResponseEntity.ok(res);
    }
}