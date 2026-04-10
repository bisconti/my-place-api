package com.record.myplace.notification.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.notification.dto.RecommendationNotificationCreateRequestDto;
import com.record.myplace.notification.dto.ReviewReminderTargetResponseDto;
import com.record.myplace.notification.entity.UserNotification;
import com.record.myplace.notification.repository.UserNotificationRepository;
import com.record.myplace.notification.service.UserNotificationCommandService;
import com.record.myplace.notification.service.UserNotificationQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserNotificationCommandServiceImpl implements UserNotificationCommandService {

    private final UserNotificationRepository userNotificationRepository;
    private final UserNotificationQueryService userNotificationQueryService;

    @Override
    public void createReviewReminderNotification(ReviewReminderTargetResponseDto targetDto) {
        boolean exists = userNotificationRepository.existsByUserEmailAndNotificationTypeAndTargetIdAndCreatedAtAfter(
                targetDto.getUserEmail(),
                "REVIEW_REMINDER",
                targetDto.getPlaceId(),
                LocalDateTime.now().minusDays(7)
        );

        if (exists) {
            return;
        }

        UserNotification entity = new UserNotification();
        entity.setUserEmail(targetDto.getUserEmail());
        entity.setNotificationType("REVIEW_REMINDER");
        entity.setTitle("리뷰를 남겨보세요");
        entity.setContent(
                targetDto.getPlaceName() != null && !targetDto.getPlaceName().isBlank()
                        ? String.format("%s에 아직 리뷰가 없어요. 방문한 경험을 기록해보세요.", targetDto.getPlaceName())
                        : "방문한 맛집에 아직 리뷰가 없어요. 경험을 기록해보세요."
        );
        entity.setTargetId(targetDto.getPlaceId());
        entity.setTargetType("PLACE");
        entity.setIsRead(false);

        userNotificationRepository.save(entity);
    }
    
    @Override
    public void createRecommendationNotification(RecommendationNotificationCreateRequestDto requestDto) {
        UserNotification notification = new UserNotification();
        notification.setUserEmail(requestDto.getUserEmail());
        notification.setNotificationType("RECOMMENDATION");
        notification.setTitle("취향에 맞는 맛집을 추천해드려요");
        notification.setContent(
                String.format("%s 카테고리의 \"%s\" 맛집을 추천해드려요.",
                        requestDto.getCategory(),
                        requestDto.getPlaceName())
        );
        notification.setTargetId(requestDto.getPlaceId());
        notification.setTargetType("PLACE");

        userNotificationRepository.save(notification);
    }

    @Override
    public void markAsRead(String userEmail, Long notificationId) {
        UserNotification entity = userNotificationRepository.findByIdAndUserEmail(notificationId, userEmail)
                .orElseThrow(() -> new IllegalArgumentException("알림이 존재하지 않습니다."));

        if (Boolean.TRUE.equals(entity.getIsRead())) {
            return;
        }

        entity.setIsRead(true);
        entity.setReadAt(LocalDateTime.now());

        userNotificationRepository.save(entity);
    }

    @Override
    public void markAllAsRead(String userEmail) {
        List<com.record.myplace.notification.dto.UserNotificationResponseDto> notifications =
                userNotificationQueryService.getNotifications(userEmail);

        for (com.record.myplace.notification.dto.UserNotificationResponseDto notification : notifications) {
            if (Boolean.TRUE.equals(notification.getIsRead())) {
                continue;
            }

            UserNotification entity = userNotificationRepository.findByIdAndUserEmail(notification.getId(), userEmail)
                    .orElse(null);

            if (entity == null) {
                continue;
            }

            entity.setIsRead(true);
            entity.setReadAt(LocalDateTime.now());
            userNotificationRepository.save(entity);
        }
    }
}