package com.record.myplace.placeLike.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.place.service.PlaceCommandService;
import com.record.myplace.placeLike.dto.PlaceLikeResponse;
import com.record.myplace.placeLike.dto.PlaceLikeToggleRequest;
import com.record.myplace.placeLike.entity.PlaceLike;
import com.record.myplace.placeLike.repository.PlaceLikeRepository;
import com.record.myplace.placeLike.service.PlaceLikeCommandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceLikeCommandServiceImpl implements PlaceLikeCommandService {

    private final PlaceLikeRepository placeLikeRepository;
    private final PlaceCommandService placeService;

    @Override
    public PlaceLikeResponse toggle(String useremail, PlaceLikeToggleRequest req) {
        if (req.getPlaceId() == null || req.getPlaceId().isBlank()) {
            throw new IllegalArgumentException("placeId는 필수입니다.");
        }

        boolean wantLike = Boolean.TRUE.equals(req.getLiked());

        if (wantLike) {
            placeService.ensurePlaceExists(
                    req.getPlaceId(),
                    req.getPlaceName(),
                    req.getAddress(),
                    req.getRoadAddress(),
                    req.getCategory(),
                    req.getPhone()
            );

            var opt = placeLikeRepository.findByUseremailAndPlaceId(useremail, req.getPlaceId());

            if (opt.isPresent()) {
                PlaceLike existing = opt.get();
                return toResponse(existing, true);
            }

            PlaceLike pl = new PlaceLike();
            pl.setUseremail(useremail);
            pl.setPlaceId(req.getPlaceId());
            pl.setPlaceName(req.getPlaceName());
            pl.setAddress(req.getAddress());
            pl.setCategory(req.getCategory());

            try {
                PlaceLike saved = placeLikeRepository.save(pl);
                return toResponse(saved, true);
            } catch (DataIntegrityViolationException e) {
                PlaceLike existing = placeLikeRepository.findByUseremailAndPlaceId(useremail, req.getPlaceId())
                        .orElseThrow(() -> e);
                return toResponse(existing, true);
            }
        }

        placeLikeRepository.deleteByUseremailAndPlaceId(useremail, req.getPlaceId());

        PlaceLikeResponse response = new PlaceLikeResponse();
        response.setPlaceId(req.getPlaceId());
        response.setPlaceName(req.getPlaceName());
        response.setAddress(req.getAddress());
        response.setCategory(req.getCategory());
        response.setCreatedAt(null);
        response.setLiked(false);

        return response;
    }

    private PlaceLikeResponse toResponse(PlaceLike entity, boolean liked) {
        PlaceLikeResponse response = new PlaceLikeResponse();
        response.setPlaceId(entity.getPlaceId());
        response.setPlaceName(entity.getPlaceName());
        response.setAddress(entity.getAddress());
        response.setCategory(entity.getCategory());
        response.setCreatedAt(entity.getCreatedAt());
        response.setLiked(liked);
        return response;
    }
}