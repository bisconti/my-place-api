package com.record.myplace.placeLike.controller;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.placeLike.dto.PlaceLikeListResponse;
import com.record.myplace.placeLike.dto.PlaceLikeResponse;
import com.record.myplace.placeLike.dto.PlaceLikeToggleRequest;
import com.record.myplace.placeLike.service.PlaceLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place-likes")
public class PlaceLikeController {

    private final PlaceLikeService placeLikeService;

    /**
     * 좋아요(찜) 토글
     * liked=true  -> 저장
     * liked=false -> 삭제
     */
    @PostMapping("/toggle")
    public ResponseEntity<PlaceLikeResponse> toggle(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody PlaceLikeToggleRequest req
    ) {
        String useremail = user.getEmail();
        PlaceLikeResponse res = placeLikeService.toggle(useremail, req);
        return ResponseEntity.ok(res);
    }

    /**
     * 내 찜 목록
     */
    @GetMapping("/me")
    public ResponseEntity<PlaceLikeListResponse> myLikes(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        String useremail = user.getEmail();
        PlaceLikeListResponse res = placeLikeService.myLikes(useremail);
        return ResponseEntity.ok(res);
    }

    /**
     * 특정 placeId 찜 여부
     * /api/place-likes/exists?placeId=KAKAO_123
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam String placeId
    ) {
        String useremail = user.getEmail();
        boolean liked = placeLikeService.exists(useremail, placeId);
        return ResponseEntity.ok(liked);
    }
}