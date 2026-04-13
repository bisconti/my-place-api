package com.record.myplace.recentplace.service;

import com.record.myplace.recentplace.dto.RecentPlaceQueryDto;

import java.util.List;

public interface RecentPlaceQueryService {

    List<RecentPlaceQueryDto> getRecentPlaces(String userEmail);
}