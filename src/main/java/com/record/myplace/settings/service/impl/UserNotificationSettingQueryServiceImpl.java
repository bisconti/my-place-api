package com.record.myplace.settings.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.settings.dto.NotificationSettingsResponseDto;
import com.record.myplace.settings.mapper.UserNotificationSettingQueryMapper;
import com.record.myplace.settings.service.UserNotificationSettingQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserNotificationSettingQueryServiceImpl implements UserNotificationSettingQueryService {

    private final UserNotificationSettingQueryMapper userNotificationSettingQueryMapper;

    @Override
    public NotificationSettingsResponseDto getNotificationSettings(String userEmail) {
        return userNotificationSettingQueryMapper.selectNotificationSettingsByUserEmail(userEmail);
    }
}
