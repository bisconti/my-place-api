package com.record.myplace.notification.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.record.myplace.notification.entity.UserNotification;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

    Optional<UserNotification> findByIdAndUserEmail(Long id, String userEmail);

    boolean existsByUserEmailAndNotificationTypeAndTargetIdAndCreatedAtAfter(
            String userEmail,
            String notificationType,
            String targetId,
            LocalDateTime createdAt
    );

    @Modifying
    @Query("""
            update UserNotification n
               set n.isRead = true,
                   n.readAt = :readAt
             where n.userEmail = :userEmail
               and n.isRead = false
            """)
    int markAllAsReadByUserEmail(@Param("userEmail") String userEmail, @Param("readAt") LocalDateTime readAt);
}
