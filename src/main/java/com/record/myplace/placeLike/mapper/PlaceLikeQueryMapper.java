package com.record.myplace.placeLike.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.record.myplace.placeLike.dto.PlaceLikeResponse;

@Mapper
public interface PlaceLikeQueryMapper {

    List<PlaceLikeResponse> selectMyLikes(@Param("useremail") String useremail);

    Integer selectExists(@Param("useremail") String useremail, @Param("placeId") String placeId);

    Long selectCount(@Param("useremail") String useremail);
}