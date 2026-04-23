package com.record.myplace.notification.scheduler;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.record.myplace.infra.redis.RedisLockRepository;
import com.record.myplace.notification.dto.ReviewReminderTargetResponseDto;
import com.record.myplace.notification.service.UserNotificationCommandService;
import com.record.myplace.notification.service.UserNotificationQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewReminderNotificationScheduler {

    private static final String LOCK_NAME = "scheduler:review-reminder-notification";

    private final UserNotificationQueryService userNotificationQueryService;
    private final UserNotificationCommandService userNotificationCommandService;
    private final RedisLockRepository redisLockRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void createReviewReminderNotifications() {
        String owner = UUID.randomUUID().toString();
        boolean locked = redisLockRepository.tryLock(LOCK_NAME, owner, Duration.ofMinutes(30));

        if (!locked) {
            log.info("리뷰 리마인드 스케줄러 락을 획득하지 못해 이번 실행을 건너뜁니다.");
            return;
        }

        try {
            List<ReviewReminderTargetResponseDto> targets = userNotificationQueryService.getReviewReminderTargets();

            if (targets.isEmpty()) {
                log.info("리뷰 리마인드 알림 생성 대상이 없습니다.");
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
        } finally {
            redisLockRepository.unlock(LOCK_NAME, owner);
        }
    }
}
