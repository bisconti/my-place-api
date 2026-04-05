package com.record.myplace.settings.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.record.myplace.settings.entity.UserNotificationSetting;

public interface UserNotificationSettingRepository extends JpaRepository<UserNotificationSetting, Long> {

    Optional<UserNotificationSetting> findByUserEmail(String userEmail);
}