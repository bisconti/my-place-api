package com.record.myplace.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.record.myplace.place.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, String> {
}