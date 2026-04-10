package com.record.myplace.notification.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.record.myplace.notification.dto.RecommendationNotificationTargetUserResponseDto;
import com.record.myplace.notification.dto.RecommendedPlaceResponseDto;
import com.record.myplace.notification.dto.UserPreferredCategoryResponseDto;

@Mapper
public interface RecommendationNotificationQueryMapper {

    /**
     * 추천 알림 수신 동의 사용자 조회
     */
    List<RecommendationNotificationTargetUserResponseDto> selectRecommendationTargetUsers();

    /**
     * 사용자 선호 카테고리 조회
     */
    List<UserPreferredCategoryResponseDto> selectUserPreferredCategories(
            @Param("userEmail") String userEmail
    );

    /**
     * 카테고리 기준 추천 식당 조회
     */
    List<RecommendedPlaceResponseDto> selectRecommendedPlaces(
            @Param("userEmail") String userEmail,
            @Param("category") String category
    );
}