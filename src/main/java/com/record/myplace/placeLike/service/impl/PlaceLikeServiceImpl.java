package com.record.myplace.placeLike.service.impl;

import com.record.myplace.placeLike.dto.PlaceLikeListResponse;
import com.record.myplace.placeLike.dto.PlaceLikeResponse;
import com.record.myplace.placeLike.dto.PlaceLikeToggleRequest;
import com.record.myplace.placeLike.entity.PlaceLike;
import com.record.myplace.placeLike.repository.PlaceLikeRepository;
import com.record.myplace.placeLike.service.PlaceLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceLikeServiceImpl implements PlaceLikeService {

    private final PlaceLikeRepository placeLikeRepository;

    @Override
    @Transactional
    public PlaceLikeResponse toggle(String useremail, PlaceLikeToggleRequest req) {
        if (req.getPlaceId() == null || req.getPlaceId().isBlank()) {
            throw new IllegalArgumentException("placeId는 필수입니다.");
        }

        boolean wantLike = Boolean.TRUE.equals(req.getLiked());

        if (wantLike) {
            var opt = placeLikeRepository.findByUseremailAndPlaceId(useremail, req.getPlaceId());
            if (opt.isPresent()) {
                PlaceLike existing = opt.get();
                return PlaceLikeResponse.builder()
                    .placeId(existing.getPlaceId())
                    .placeName(existing.getPlaceName())
                    .address(existing.getAddress())
                    .category(existing.getCategory())
                    .createdAt(existing.getCreatedAt())
                    .liked(true)
                    .build();
            }

            PlaceLike pl = new PlaceLike();
            pl.setUseremail(useremail);
            pl.setPlaceId(req.getPlaceId());
            pl.setPlaceName(req.getPlaceName());
            pl.setAddress(req.getAddress());
            pl.setCategory(req.getCategory());

            try {
                PlaceLike saved = placeLikeRepository.save(pl);
                return PlaceLikeResponse.builder()
                    .placeId(saved.getPlaceId())
                    .placeName(saved.getPlaceName())
                    .address(saved.getAddress())
                    .category(saved.getCategory())
                    .createdAt(saved.getCreatedAt())
                    .liked(true)
                    .build();
            } catch (DataIntegrityViolationException e) {
                PlaceLike ex = placeLikeRepository.findByUseremailAndPlaceId(useremail, req.getPlaceId())
                    .orElseThrow(() -> e);
                return PlaceLikeResponse.builder()
                    .placeId(ex.getPlaceId())
                    .placeName(ex.getPlaceName())
                    .address(ex.getAddress())
                    .category(ex.getCategory())
                    .createdAt(ex.getCreatedAt())
                    .liked(true)
                    .build();
            }
        } else {
            // 해제
            placeLikeRepository.deleteByUseremailAndPlaceId(useremail, req.getPlaceId());
            return PlaceLikeResponse.builder()
                .placeId(req.getPlaceId())
                .placeName(req.getPlaceName())
                .address(req.getAddress())
                .category(req.getCategory())
                .createdAt(null)
                .liked(false)
                .build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceLikeListResponse myLikes(String useremail) {
        var list = placeLikeRepository.findAllByUseremailOrderByCreatedAtDesc(useremail)
            .stream()
            .map(pl -> PlaceLikeResponse.builder()
                .placeId(pl.getPlaceId())
                .placeName(pl.getPlaceName())
                .address(pl.getAddress())
                .category(pl.getCategory())
                .createdAt(pl.getCreatedAt())
                .liked(true)
                .build())
            .collect(Collectors.toList());

        return PlaceLikeListResponse.builder().items(list).build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(String useremail, String placeId) {
        return placeLikeRepository.existsByUseremailAndPlaceId(useremail, placeId);
    }
}