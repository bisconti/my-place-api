package com.record.myplace.notification.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.record.myplace.notification.dto.ReviewReminderTargetResponseDto;
import com.record.myplace.notification.service.UserNotificationCommandService;
import com.record.myplace.notification.service.UserNotificationQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewReminderNotificationScheduler {

    private final UserNotificationQueryService userNotificationQueryService;
    private final UserNotificationCommandService userNotificationCommandService;

    @Scheduled(cron = "0 0 3 * * *")
    public void createReviewReminderNotifications() {
        List<ReviewReminderTargetResponseDto> targets = userNotificationQueryService.getReviewReminderTargets();

        if (targets.isEmpty()) {
            log.info("리뷰 리마인드 알림 생성 대상 없음");
            return;
        }

        for (ReviewReminderTargetResponseDto target : targets) {
            try {
                userNotificationCommandService.createReviewReminderNotification(target);
            } catch (Exception e) {
                log.error("리뷰 리마인드 알림 생성 실패. userEmail={}, placeId={}",
                        target.getUserEmail(), target.getPlaceId(), e);
            }
        }

        log.info("리뷰 리마인드 알림 생성 완료. count={}", targets.size());
    }
}