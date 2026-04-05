package com.record.myplace.placeVisitHistory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.record.myplace.placeVisitHistory.dto.PlaceVisitHistoryResponseDto;
import com.record.myplace.placeVisitHistory.dto.PlaceVisitedResponseDto;

@Mapper
public interface PlaceVisitHistoryQueryMapper {

    List<PlaceVisitHistoryResponseDto> selectVisitHistoriesByUserEmail(@Param("userEmail") String userEmail);

    PlaceVisitedResponseDto selectVisitedByUserEmailAndPlaceId(@Param("userEmail") String userEmail,
                                                               @Param("placeId") String placeId);
}