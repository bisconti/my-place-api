package com.record.myplace.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.record.myplace.auth.dto.AuthUserQueryDto;
import com.record.myplace.auth.dto.PasswordResetTokenQueryDto;

@Mapper
public interface AuthQueryMapper {

    AuthUserQueryDto selectUserByEmail(@Param("email") String email);

    Integer selectUserCountByEmail(@Param("email") String email);

    PasswordResetTokenQueryDto selectPasswordResetTokenByToken(@Param("token") String token);
}