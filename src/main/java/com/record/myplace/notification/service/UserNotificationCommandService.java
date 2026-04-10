package com.record.myplace.notification.service;

import com.record.myplace.notification.dto.RecommendationNotificationCreateRequestDto;
import com.record.myplace.notification.dto.ReviewReminderTargetResponseDto;

public interface UserNotificationCommandService {

    void createReviewReminderNotification(ReviewReminderTargetResponseDto targetDto);

    void createRecommendationNotification(RecommendationNotificationCreateRequestDto requestDto);

    void markAsRead(String userEmail, Long notificationId);
    
    void markAllAsRead(String userEmail);
}