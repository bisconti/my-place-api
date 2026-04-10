package com.record.myplace.notification.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.record.myplace.notification.service.RecommendationNotificationQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationNotificationScheduler {

    private final RecommendationNotificationQueryService recommendationNotificationQueryService;

    @Scheduled(cron = "0 0 10 * * *")
    public void createRecommendationNotifications() {
        log.info("추천 알림 스케줄러 시작");
        recommendationNotificationQueryService.createRecommendationNotifications();
        log.info("추천 알림 스케줄러 종료");
    }
}