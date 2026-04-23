package com.record.myplace.notification.service.impl;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.record.myplace.notification.dto.UnreadNotificationCountResponseDto;
import com.record.myplace.notification.mapper.UserNotificationQueryMapper;
import com.record.myplace.notification.service.NotificationUnreadCountCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationUnreadCountCacheServiceImpl implements NotificationUnreadCountCacheService {

    private static final Duration TTL = Duration.ofHours(6);
    private static final String KEY_PREFIX = "notification:unread-count:";

    private final StringRedisTemplate redisTemplate;
    private final UserNotificationQueryMapper userNotificationQueryMapper;

    @Override
    public Integer getUnreadCount(String userEmail) {
        try {
            String cachedValue = redisTemplate.opsForValue().get(getKey(userEmail));
            if (cachedValue != null) {
                return Integer.parseInt(cachedValue);
            }

            Integer unreadCount = loadUnreadCountFromDb(userEmail);
            redisTemplate.opsForValue().set(getKey(userEmail), String.valueOf(unreadCount), TTL);
            return unreadCount;
        } catch (RuntimeException ex) {
            log.warn("Unread count Redis 조회 실패. userEmail={}, fallback=DB", userEmail, ex);
            return loadUnreadCountFromDb(userEmail);
        }
    }

    @Override
    public void increase(String userEmail) {
        try {
            String key = getKey(userEmail);

            if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                Integer unreadCount = loadUnreadCountFromDb(userEmail);
                redisTemplate.opsForValue().set(key, String.valueOf(unreadCount), TTL);
                return;
            }

            redisTemplate.opsForValue().increment(key);
            redisTemplate.expire(key, TTL);
        } catch (RuntimeException ex) {
            log.warn("Unread count Redis 증가 실패. userEmail={}", userEmail, ex);
        }
    }

    @Override
    public void decrease(String userEmail) {
        try {
            String key = getKey(userEmail);

            if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                Integer unreadCount = loadUnreadCountFromDb(userEmail);
                redisTemplate.opsForValue().set(key, String.valueOf(unreadCount), TTL);
                return;
            }

            Long nextValue = redisTemplate.opsForValue().decrement(key);
            if (nextValue != null && nextValue < 0) {
                redisTemplate.opsForValue().set(key, "0", TTL);
                return;
            }

            redisTemplate.expire(key, TTL);
        } catch (RuntimeException ex) {
            log.warn("Unread count Redis 감소 실패. userEmail={}", userEmail, ex);
        }
    }

    @Override
    public void reset(String userEmail) {
        try {
            redisTemplate.opsForValue().set(getKey(userEmail), "0", TTL);
        } catch (RuntimeException ex) {
            log.warn("Unread count Redis 초기화 실패. userEmail={}", userEmail, ex);
        }
    }

    private Integer loadUnreadCountFromDb(String userEmail) {
        UnreadNotificationCountResponseDto response = userNotificationQueryMapper.selectUnreadNotificationCountByUserEmail(userEmail);
        return response != null && response.getUnreadCount() != null ? response.getUnreadCount() : 0;
    }

    private String getKey(String userEmail) {
        return KEY_PREFIX + userEmail;
    }
}
