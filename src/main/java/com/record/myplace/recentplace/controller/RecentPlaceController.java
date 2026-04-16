package com.record.myplace.recentplace.controller;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.recentplace.dto.RecentPlaceCommandDto;
import com.record.myplace.recentplace.dto.RecentPlaceQueryDto;
import com.record.myplace.recentplace.service.RecentPlaceCommandService;
import com.record.myplace.recentplace.service.RecentPlaceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/recent-places")
@RequiredArgsConstructor
public class RecentPlaceController {

    private final RecentPlaceCommandService commandService;
    private final RecentPlaceQueryService queryService;

    @PostMapping
    public void saveRecentPlace(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody RecentPlaceCommandDto dto
    ) {
        if (user == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "로그인이 필요합니다.");
        }
        if (dto == null || !StringUtils.hasText(dto.getPlaceId())) {
            throw new ResponseStatusException(BAD_REQUEST, "placeId는 필수입니다.");
        }

        dto.setUserEmail(user.getEmail());
        commandService.saveRecentPlace(dto);
    }

    @GetMapping
    public List<RecentPlaceQueryDto> getRecentPlaces(@AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return queryService.getRecentPlaces(user.getEmail());
    }
}
