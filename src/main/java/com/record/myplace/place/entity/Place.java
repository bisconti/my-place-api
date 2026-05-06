package com.record.myplace.place.entity;

import java.util.ArrayList;
import java.util.List;

import com.record.myplace.placeReview.entity.PlaceReview;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "place")
@Getter
@Setter
@NoArgsConstructor
public class Place {

    @Id
    @Column(name = "place_id", length = 64)
    private String placeId;

    @Column(name = "place_name", nullable = false, length = 255)
    private String placeName;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "road_address", length = 500)
    private String roadAddress;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "featured_live_info_tv", nullable = false)
    private Boolean featuredLiveInfoTv = false;

    @Column(name = "featured_life_master", nullable = false)
    private Boolean featuredLifeMaster = false;

    @Column(name = "featured_baekban_trip", nullable = false)
    private Boolean featuredBaekbanTrip = false;

    @OneToMany(mappedBy = "place")
    private List<PlaceReview> reviews = new ArrayList<>();
}
