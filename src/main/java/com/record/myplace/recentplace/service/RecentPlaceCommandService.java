package com.record.myplace.recentplace.service;

import com.record.myplace.recentplace.dto.RecentPlaceCommandDto;

public interface RecentPlaceCommandService {

    void saveRecentPlace(RecentPlaceCommandDto dto);
}