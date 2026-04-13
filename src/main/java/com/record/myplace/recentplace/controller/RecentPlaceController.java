package com.record.myplace.recentplace.controller;

import com.record.myplace.recentplace.dto.RecentPlaceCommandDto;
import com.record.myplace.recentplace.dto.RecentPlaceQueryDto;
import com.record.myplace.recentplace.service.RecentPlaceCommandService;
import com.record.myplace.recentplace.service.RecentPlaceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recent-places")
@RequiredArgsConstructor
public class RecentPlaceController {

    private final RecentPlaceCommandService commandService;
    private final RecentPlaceQueryService queryService;

    @PostMapping
    public void saveRecentPlace(@RequestBody RecentPlaceCommandDto dto) {
        commandService.saveRecentPlace(dto);
    }

    @GetMapping
    public List<RecentPlaceQueryDto> getRecentPlaces(@RequestParam String userEmail) {
        return queryService.getRecentPlaces(userEmail);
    }
}