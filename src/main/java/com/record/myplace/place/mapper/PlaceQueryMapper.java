package com.record.myplace.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.record.myplace.place.dto.PlaceDetailWithImageRowDto;

@Mapper
public interface PlaceQueryMapper {

	List<PlaceDetailWithImageRowDto> selectPlaceDetailByPlaceId(@Param("placeId") String placeId,
                                                      @Param("userEmail") String userEmail);
}