package com.record.myplace.settings.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_notification_setting")
public class UserNotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = false, unique = true, length = 255)
    private String userEmail;

    @Column(name = "review_reminder_enabled", nullable = false)
    private Boolean reviewReminderEnabled;

    @Column(name = "favorite_place_event_enabled", nullable = false)
    private Boolean favoritePlaceEventEnabled;

    @Column(name = "recommendation_enabled", nullable = false)
    private Boolean recommendationEnabled;

    @Column(name = "visit_reminder_enabled", nullable = false)
    private Boolean visitReminderEnabled;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.reviewReminderEnabled == null) this.reviewReminderEnabled = true;
        if (this.favoritePlaceEventEnabled == null) this.favoritePlaceEventEnabled = true;
        if (this.recommendationEnabled == null) this.recommendationEnabled = true;
        if (this.visitReminderEnabled == null) this.visitReminderEnabled = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
