package com.record.myplace.placeReview.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.record.myplace.place.entity.Place;
import com.record.myplace.user.entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "place_review")
@Getter
@Setter
@NoArgsConstructor
public class PlaceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "useremail", nullable = false, length = 255)
    private String userEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "useremail", referencedColumnName = "useremail", insertable = false, updatable = false)
    private User user;

    @Column(name = "place_id", nullable = false, length = 64)
    private String placeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", referencedColumnName = "place_id", insertable = false, updatable = false)
    private Place place;
    
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceReviewImage> images = new ArrayList<>();
}