package com.record.myplace.user.service;

import com.record.myplace.user.dto.MeResponse;

public interface UserQueryService {
    MeResponse getMe(String email);
}