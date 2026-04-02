package com.record.myplace.placeLike.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.placeLike.dto.PlaceLikeListResponse;
import com.record.myplace.placeLike.dto.PlaceLikeResponse;
import com.record.myplace.placeLike.mapper.PlaceLikeQueryMapper;
import com.record.myplace.placeLike.service.PlaceLikeQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceLikeQueryServiceImpl implements PlaceLikeQueryService {

    private final PlaceLikeQueryMapper placeLikeQueryMapper;

    @Override
    public PlaceLikeListResponse myLikes(String useremail) {
        List<PlaceLikeResponse> list = placeLikeQueryMapper.selectMyLikes(useremail);

        PlaceLikeListResponse response = new PlaceLikeListResponse();
        response.setItems(list);

        return response;
    }

    @Override
    public boolean exists(String useremail, String placeId) {
        Integer count = placeLikeQueryMapper.selectExists(useremail, placeId);
        return count != null && count > 0;
    }

    @Override
    public long count(String useremail) {
        Long count = placeLikeQueryMapper.selectCount(useremail);
        return count != null ? count : 0L;
    }
}