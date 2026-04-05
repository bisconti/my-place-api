package com.record.myplace.settings.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.record.myplace.settings.dto.NotificationSettingsResponseDto;

@Mapper
public interface UserNotificationSettingQueryMapper {

    NotificationSettingsResponseDto selectNotificationSettingsByUserEmail(@Param("userEmail") String userEmail);
}