package com.record.myplace.place.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.place.entity.Place;
import com.record.myplace.place.repository.PlaceRepository;
import com.record.myplace.place.service.PlaceCommandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceCommandServiceImpl implements PlaceCommandService {

    private final PlaceRepository placeRepository;

    @Override
    public void ensurePlaceExists(
            String placeId,
            String placeName,
            String address,
            String roadAddress,
            String category,
            String phone
    ) {
        if (placeId == null || placeId.isBlank()) {
            throw new IllegalArgumentException("placeId는 필수입니다.");
        }

        Place place = placeRepository.findById(placeId).orElse(null);

        if (place == null) {
            Place newPlace = new Place();
            newPlace.setPlaceId(placeId);
            newPlace.setPlaceName(placeName);
            newPlace.setAddress(address);
            newPlace.setRoadAddress(roadAddress);
            newPlace.setCategory(category);
            newPlace.setPhone(phone);

            placeRepository.save(newPlace);
            return;
        }

        boolean changed = false;

        if (isDifferent(place.getPlaceName(), placeName) && placeName != null && !placeName.isBlank()) {
            place.setPlaceName(placeName);
            changed = true;
        }

        if (isDifferent(place.getAddress(), address)) {
            place.setAddress(address);
            changed = true;
        }

        if (isDifferent(place.getRoadAddress(), roadAddress)) {
            place.setRoadAddress(roadAddress);
            changed = true;
        }

        if (isDifferent(place.getCategory(), category)) {
            place.setCategory(category);
            changed = true;
        }

        if (isDifferent(place.getPhone(), phone)) {
            place.setPhone(phone);
            changed = true;
        }

        if (changed) {
            placeRepository.save(place);
        }
    }

    private boolean isDifferent(String oldValue, String newValue) {
        if (oldValue == null && newValue == null) {
            return false;
        }
        if (oldValue == null || newValue == null) {
            return true;
        }
        return !oldValue.equals(newValue);
    }
}