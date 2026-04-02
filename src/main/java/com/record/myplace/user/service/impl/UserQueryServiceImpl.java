package com.record.myplace.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.user.dto.MeResponse;
import com.record.myplace.user.mapper.UserQueryMapper;
import com.record.myplace.user.service.UserQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserQueryMapper userQueryMapper;

    @Override
    public MeResponse getMe(String email) {
        return userQueryMapper.selectMeByEmail(email);
    }
}