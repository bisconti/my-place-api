package com.record.myplace.notification.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.notification.dto.ReviewReminderTargetResponseDto;
import com.record.myplace.notification.dto.UnreadNotificationCountResponseDto;
import com.record.myplace.notification.dto.UserNotificationResponseDto;
import com.record.myplace.notification.mapper.UserNotificationQueryMapper;
import com.record.myplace.notification.service.UserNotificationQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserNotificationQueryServiceImpl implements UserNotificationQueryService {

    private final UserNotificationQueryMapper userNotificationQueryMapper;

    @Override
    public List<UserNotificationResponseDto> getNotifications(String userEmail) {
        return userNotificationQueryMapper.selectNotificationsByUserEmail(userEmail);
    }

    @Override
    public UnreadNotificationCountResponseDto getUnreadNotificationCount(String userEmail) {
        return userNotificationQueryMapper.selectUnreadNotificationCountByUserEmail(userEmail);
    }

    @Override
    public List<ReviewReminderTargetResponseDto> getReviewReminderTargets() {
        return userNotificationQueryMapper.selectReviewReminderTargets();
    }
}