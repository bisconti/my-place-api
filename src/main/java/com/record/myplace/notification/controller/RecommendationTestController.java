package com.record.myplace.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.myplace.notification.service.RecommendationNotificationQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test/recommendation")
public class RecommendationTestController {

    private final RecommendationNotificationQueryService recommendationNotificationQueryService;

    @GetMapping("/run")
    public String runRecommendation() {
        recommendationNotificationQueryService.createRecommendationNotifications();
        return "추천 알림 생성 완료";
    }
}
