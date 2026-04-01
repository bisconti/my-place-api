package com.record.myplace.placeReview.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.record.myplace.placeReview.dto.PlaceReviewImageResponseDto;
import com.record.myplace.placeReview.dto.PlaceReviewItemDto;
import com.record.myplace.placeReview.dto.PlaceReviewSummaryDto;

@Mapper
public interface PlaceReviewMapper {

    List<PlaceReviewItemDto> selectMyReviews(@Param("userEmail") String userEmail);

    List<PlaceReviewItemDto> selectReviewsByPlaceId(@Param("placeId") String placeId);

    PlaceReviewSummaryDto selectReviewSummary(@Param("placeId") String placeId);

    List<PlaceReviewImageResponseDto> selectReviewImagesByReviewIds(@Param("reviewIds") List<Long> reviewIds);
}