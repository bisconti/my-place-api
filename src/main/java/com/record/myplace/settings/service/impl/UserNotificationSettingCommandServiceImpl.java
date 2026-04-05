package com.record.myplace.settings.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.settings.dto.NotificationSettingsRequestDto;
import com.record.myplace.settings.entity.UserNotificationSetting;
import com.record.myplace.settings.repository.UserNotificationSettingRepository;
import com.record.myplace.settings.service.UserNotificationSettingCommandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserNotificationSettingCommandServiceImpl implements UserNotificationSettingCommandService {

    private final UserNotificationSettingRepository userNotificationSettingRepository;

    @Override
    public void createDefaultNotificationSettingsIfNotExists(String userEmail) {
        boolean exists = userNotificationSettingRepository.findByUserEmail(userEmail).isPresent();

        if (exists) {
            return;
        }

        UserNotificationSetting entity = new UserNotificationSetting();
        entity.setUserEmail(userEmail);
        entity.setReviewReminderEnabled(true);
        entity.setFavoritePlaceEventEnabled(true);
        entity.setRecommendationEnabled(true);
        entity.setVisitReminderEnabled(true);

        userNotificationSettingRepository.save(entity);
    }

    @Override
    public void saveNotificationSettings(String userEmail, NotificationSettingsRequestDto requestDto) {
        UserNotificationSetting entity = userNotificationSettingRepository.findByUserEmail(userEmail)
                .orElseGet(() -> {
                    UserNotificationSetting newEntity = new UserNotificationSetting();
                    newEntity.setUserEmail(userEmail);
                    return newEntity;
                });

        entity.setReviewReminderEnabled(
                requestDto.getReviewReminderEnabled() != null ? requestDto.getReviewReminderEnabled() : true
        );
        entity.setFavoritePlaceEventEnabled(
                requestDto.getFavoritePlaceEventEnabled() != null ? requestDto.getFavoritePlaceEventEnabled() : true
        );
        entity.setRecommendationEnabled(
                requestDto.getRecommendationEnabled() != null ? requestDto.getRecommendationEnabled() : true
        );
        entity.setVisitReminderEnabled(
                requestDto.getVisitReminderEnabled() != null ? requestDto.getVisitReminderEnabled() : true
        );

        userNotificationSettingRepository.save(entity);
    }
}