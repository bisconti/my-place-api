package com.record.myplace.settings.service;

import com.record.myplace.settings.dto.NotificationSettingsRequestDto;

public interface UserNotificationSettingCommandService {

    void createDefaultNotificationSettingsIfNotExists(String userEmail);

    void saveNotificationSettings(String userEmail, NotificationSettingsRequestDto requestDto);
}
