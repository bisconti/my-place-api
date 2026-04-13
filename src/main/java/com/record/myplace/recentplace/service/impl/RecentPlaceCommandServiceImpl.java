package com.record.myplace.recentplace.service.impl;

import com.record.myplace.recentplace.dto.RecentPlaceCommandDto;
import com.record.myplace.recentplace.entity.RecentPlace;
import com.record.myplace.recentplace.repository.RecentPlaceRepository;
import com.record.myplace.recentplace.service.RecentPlaceCommandService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecentPlaceCommandServiceImpl implements RecentPlaceCommandService {

    private final RecentPlaceRepository repository;

    @Override
    public void saveRecentPlace(RecentPlaceCommandDto dto) {

        repository.findByUserEmailAndPlaceId(dto.getUserEmail(), dto.getPlaceId())
                .ifPresentOrElse(existing -> {
                    existing.setViewedAt(LocalDateTime.now());
                    repository.save(existing);
                }, () -> {
                    RecentPlace entity = new RecentPlace();
                    entity.setUserEmail(dto.getUserEmail());
                    entity.setPlaceId(dto.getPlaceId());
                    entity.setViewedAt(LocalDateTime.now());
                    repository.save(entity);
                });
    }
}