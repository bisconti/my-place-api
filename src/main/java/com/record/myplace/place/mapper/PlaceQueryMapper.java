package com.record.myplace.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.record.myplace.place.dto.PlaceAutoCompleteResponseDto;
import com.record.myplace.place.dto.PlaceDetailWithImageRowDto;
import com.record.myplace.place.dto.PlaceListItemResponseDto;

@Mapper
public interface PlaceQueryMapper {

	List<PlaceDetailWithImageRowDto> selectPlaceDetailByPlaceId(@Param("placeId") String placeId,
                                                      @Param("userEmail") String userEmail);
	
    List<PlaceAutoCompleteResponseDto> selectPlaceAutoCompleteList(@Param("keyword") String keyword);

    List<PlaceListItemResponseDto> selectPlaceListMetadata(@Param("placeIds") List<String> placeIds,
                                                           @Param("userEmail") String userEmail);
}
