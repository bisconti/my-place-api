package com.record.myplace.notification.service;

import com.record.myplace.notification.dto.ReviewReminderTargetResponseDto;

public interface UserNotificationCommandService {

    void createReviewReminderNotification(ReviewReminderTargetResponseDto targetDto);

    void markAsRead(String userEmail, Long notificationId);

    void markAllAsRead(String userEmail);
}