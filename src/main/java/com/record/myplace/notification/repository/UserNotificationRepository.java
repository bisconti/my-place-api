package com.record.myplace.notification.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.record.myplace.notification.entity.UserNotification;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

    Optional<UserNotification> findByIdAndUserEmail(Long id, String userEmail);

    boolean existsByUserEmailAndNotificationTypeAndTargetIdAndCreatedAtAfter(
            String userEmail,
            String notificationType,
            String targetId,
            LocalDateTime createdAt
    );
}