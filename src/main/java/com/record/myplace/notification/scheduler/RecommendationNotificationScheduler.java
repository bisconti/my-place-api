package com.record.myplace.notification.scheduler;

import java.time.Duration;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.record.myplace.infra.redis.RedisLockRepository;
import com.record.myplace.notification.service.RecommendationNotificationQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationNotificationScheduler {

    private static final String LOCK_NAME = "scheduler:recommendation-notification";

    private final RecommendationNotificationQueryService recommendationNotificationQueryService;
    private final RedisLockRepository redisLockRepository;

    @Scheduled(cron = "0 0 10 * * *")
    public void createRecommendationNotifications() {
        String owner = UUID.randomUUID().toString();
        boolean locked = redisLockRepository.tryLock(LOCK_NAME, owner, Duration.ofMinutes(30));

        if (!locked) {
            log.info("추천 알림 스케줄러 락을 획득하지 못해 이번 실행을 건너뜁니다.");
            return;
        }

        try {
            log.info("추천 알림 스케줄러 시작");
            recommendationNotificationQueryService.createRecommendationNotifications();
            log.info("추천 알림 스케줄러 종료");
        } finally {
            redisLockRepository.unlock(LOCK_NAME, owner);
        }
    }
}
