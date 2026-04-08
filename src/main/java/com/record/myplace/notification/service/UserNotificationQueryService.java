package com.record.myplace.notification.service;

import java.util.List;

import com.record.myplace.notification.dto.ReviewReminderTargetResponseDto;
import com.record.myplace.notification.dto.UnreadNotificationCountResponseDto;
import com.record.myplace.notification.dto.UserNotificationResponseDto;

public interface UserNotificationQueryService {

    List<UserNotificationResponseDto> getNotifications(String userEmail);

    UnreadNotificationCountResponseDto getUnreadNotificationCount(String userEmail);

    List<ReviewReminderTargetResponseDto> getReviewReminderTargets();
}