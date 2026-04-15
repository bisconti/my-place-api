package com.record.myplace.place.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.record.myplace.place.dto.PlaceAutoCompleteResponseDto;
import com.record.myplace.place.dto.PlaceDetailResponseDto;
import com.record.myplace.place.dto.PlaceDetailWithImageRowDto;
import com.record.myplace.place.dto.PlaceImageResponseDto;
import com.record.myplace.place.mapper.PlaceQueryMapper;
import com.record.myplace.place.service.PlaceQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PlaceQueryServiceImpl implements PlaceQueryService {

    private final PlaceQueryMapper placeQueryMapper;

    @Override
    public PlaceDetailResponseDto getPlaceDetail(String placeId, String userEmail) {
        log.info("식당 상세 조회 서비스 시작: placeId={}, userEmail={}", placeId, userEmail);

        List<PlaceDetailWithImageRowDto> rows = placeQueryMapper.selectPlaceDetailByPlaceId(placeId, userEmail);

        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 식당입니다. placeId=" + placeId);
        }

        PlaceDetailResponseDto response = new PlaceDetailResponseDto();
        List<PlaceImageResponseDto> images = new ArrayList<>();

        for (PlaceDetailWithImageRowDto row : rows) {
            // place 기본정보는 첫 row 기준으로 세팅
            if (response.getId() == null) {
                response.setId(row.getPlaceId());
                response.setName(row.getPlaceName());
                response.setAddress(row.getAddress());
                response.setRoadAddress(row.getRoadAddress());
                response.setCategory(row.getCategory());
                response.setPhone(row.getPhone());
                response.setLat(row.getLatitude());
                response.setLng(row.getLongitude());
                response.setLiked(row.getLiked());
                response.setDistanceM(row.getDistanceM());
            }

            // 이미지가 있는 경우만 추가
            if (row.getImageId() != null) {
                PlaceImageResponseDto image = new PlaceImageResponseDto();
                image.setId(row.getImageId());
                image.setPlaceId(row.getPlaceId());
                image.setOriginalFileName(row.getOriginalFileName());
                image.setStoredFileName(row.getStoredFileName());
                image.setFilePath(row.getFilePath());
                image.setFileSize(row.getFileSize());
                image.setSortOrder(row.getSortOrder());

                images.add(image);
            }
        }

        response.setImages(images);

        log.info("식당 상세 조회 서비스 종료: placeId={}, imageCount={}", placeId, images.size());
        return response;
    }
    
    @Override
    public List<PlaceAutoCompleteResponseDto> getPlaceAutoCompleteList(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return Collections.emptyList();
        }

        String trimmedKeyword = keyword.trim();

        if (trimmedKeyword.length() < 1) {
            return Collections.emptyList();
        }

        return placeQueryMapper.selectPlaceAutoCompleteList(trimmedKeyword);
    }
}