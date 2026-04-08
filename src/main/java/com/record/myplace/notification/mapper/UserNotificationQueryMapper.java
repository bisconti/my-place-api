package com.record.myplace.notification.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.record.myplace.notification.dto.ReviewReminderTargetResponseDto;
import com.record.myplace.notification.dto.UnreadNotificationCountResponseDto;
import com.record.myplace.notification.dto.UserNotificationResponseDto;

@Mapper
public interface UserNotificationQueryMapper {

    List<UserNotificationResponseDto> selectNotificationsByUserEmail(@Param("userEmail") String userEmail);

    UnreadNotificationCountResponseDto selectUnreadNotificationCountByUserEmail(@Param("userEmail") String userEmail);

    List<ReviewReminderTargetResponseDto> selectReviewReminderTargets();
}