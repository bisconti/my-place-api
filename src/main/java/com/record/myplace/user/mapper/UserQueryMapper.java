package com.record.myplace.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.record.myplace.user.dto.MeResponse;

@Mapper
public interface UserQueryMapper {

    MeResponse selectMeByEmail(@Param("email") String email);
}