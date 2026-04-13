package com.record.myplace.recentplace.mapper;

import com.record.myplace.recentplace.dto.RecentPlaceQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecentPlaceMapper {

    List<RecentPlaceQueryDto> selectRecentPlaces(@Param("userEmail") String userEmail);
}