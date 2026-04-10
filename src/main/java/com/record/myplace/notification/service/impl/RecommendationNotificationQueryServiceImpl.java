package com.record.myplace.notification.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.notification.dto.RecommendationNotificationCreateRequestDto;
import com.record.myplace.notification.dto.RecommendationNotificationTargetUserResponseDto;
import com.record.myplace.notification.dto.RecommendedPlaceResponseDto;
import com.record.myplace.notification.dto.UserPreferredCategoryResponseDto;
import com.record.myplace.notification.mapper.RecommendationNotificationQueryMapper;
import com.record.myplace.notification.service.RecommendationNotificationQueryService;
import com.record.myplace.notification.service.UserNotificationCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecommendationNotificationQueryServiceImpl implements RecommendationNotificationQueryService {

    private final RecommendationNotificationQueryMapper recommendationNotificationQueryMapper;
    private final UserNotificationCommandService userNotificationCommandService;

    @Override
    public void createRecommendationNotifications() {
        List<RecommendationNotificationTargetUserResponseDto> targetUsers =
                recommendationNotificationQueryMapper.selectRecommendationTargetUsers();

        if (targetUsers == null || targetUsers.isEmpty()) {
            log.info("추천 알림 대상 사용자가 없습니다.");
            return;
        }

        for (RecommendationNotificationTargetUserResponseDto targetUser : targetUsers) {
            createRecommendationNotificationForUser(targetUser.getUserEmail());
        }
    }

    private void createRecommendationNotificationForUser(String userEmail) {
        List<UserPreferredCategoryResponseDto> preferredCategories =
                recommendationNotificationQueryMapper.selectUserPreferredCategories(userEmail);

        if (preferredCategories == null || preferredCategories.isEmpty()) {
            log.info("선호 카테고리가 없습니다. userEmail={}", userEmail);
            return;
        }

        for (UserPreferredCategoryResponseDto preferredCategory : preferredCategories) {
            List<RecommendedPlaceResponseDto> recommendedPlaces =
                    recommendationNotificationQueryMapper.selectRecommendedPlaces(
                            userEmail,
                            preferredCategory.getCategory()
                    );

            if (recommendedPlaces == null || recommendedPlaces.isEmpty()) {
                continue;
            }

            RecommendedPlaceResponseDto place = recommendedPlaces.get(0);

            RecommendationNotificationCreateRequestDto requestDto =
                    new RecommendationNotificationCreateRequestDto();
            requestDto.setUserEmail(userEmail);
            requestDto.setPlaceId(place.getPlaceId());
            requestDto.setPlaceName(place.getPlaceName());
            requestDto.setCategory(place.getCategory());

            userNotificationCommandService.createRecommendationNotification(requestDto);

            log.info("추천 알림 생성 완료: userEmail={}, placeId={}", userEmail, place.getPlaceId());
            return;
        }

        log.info("추천 가능한 식당이 없습니다. userEmail={}", userEmail);
    }
}