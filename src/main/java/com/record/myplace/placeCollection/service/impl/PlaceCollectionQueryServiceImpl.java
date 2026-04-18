package com.record.myplace.placeCollection.service.impl;

import com.record.myplace.placeCollection.dto.PlaceCollectionDetailResponse;
import com.record.myplace.placeCollection.dto.PlaceCollectionItemResponse;
import com.record.myplace.placeCollection.dto.PlaceCollectionListResponse;
import com.record.myplace.placeCollection.dto.PlaceCollectionResponse;
import com.record.myplace.placeCollection.mapper.PlaceCollectionQueryMapper;
import com.record.myplace.placeCollection.service.PlaceCollectionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceCollectionQueryServiceImpl implements PlaceCollectionQueryService {

    private final PlaceCollectionQueryMapper placeCollectionQueryMapper;

    @Override
    public PlaceCollectionListResponse getMyCollections(String useremail, String placeId) {
        List<PlaceCollectionResponse> items = placeCollectionQueryMapper.selectMyCollections(useremail, placeId);

        PlaceCollectionListResponse response = new PlaceCollectionListResponse();
        response.setItems(items);
        return response;
    }

    @Override
    public PlaceCollectionDetailResponse getCollectionDetail(String useremail, Long collectionId) {
        PlaceCollectionResponse detail = placeCollectionQueryMapper.selectCollectionDetail(useremail, collectionId);
        if (detail == null) {
            throw new IllegalArgumentException("저장 리스트를 찾을 수 없습니다.");
        }

        List<PlaceCollectionItemResponse> items = placeCollectionQueryMapper.selectCollectionItems(useremail, collectionId);

        PlaceCollectionDetailResponse response = new PlaceCollectionDetailResponse();
        response.setCollectionId(detail.getCollectionId());
        response.setName(detail.getName());
        response.setPlaceCount(detail.getPlaceCount());
        response.setCreatedAt(detail.getCreatedAt());
        response.setUpdatedAt(detail.getUpdatedAt());
        response.setItems(items);
        return response;
    }
}
