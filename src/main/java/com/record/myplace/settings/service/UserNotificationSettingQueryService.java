package com.record.myplace.settings.service;

import com.record.myplace.settings.dto.NotificationSettingsResponseDto;

public interface UserNotificationSettingQueryService {

    NotificationSettingsResponseDto getNotificationSettings(String userEmail);
}