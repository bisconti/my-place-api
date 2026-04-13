package com.record.myplace.recentplace.service.impl;

import com.record.myplace.recentplace.dto.RecentPlaceQueryDto;
import com.record.myplace.recentplace.mapper.RecentPlaceMapper;
import com.record.myplace.recentplace.service.RecentPlaceQueryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentPlaceQueryServiceImpl implements RecentPlaceQueryService {

    private final RecentPlaceMapper mapper;

    @Override
    public List<RecentPlaceQueryDto> getRecentPlaces(String userEmail) {
        return mapper.selectRecentPlaces(userEmail);
    }
}