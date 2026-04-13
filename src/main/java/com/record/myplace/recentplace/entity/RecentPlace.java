package com.record.myplace.recentplace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "recent_place",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_email", "place_id"}))
@Getter
@Setter
public class RecentPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "place_id", nullable = false)
    private String placeId;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;
}