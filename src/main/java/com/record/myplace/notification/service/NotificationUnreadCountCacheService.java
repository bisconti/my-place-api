package com.record.myplace.notification.service;

public interface NotificationUnreadCountCacheService {

    Integer getUnreadCount(String userEmail);

    void increase(String userEmail);

    void decrease(String userEmail);

    void reset(String userEmail);
}
