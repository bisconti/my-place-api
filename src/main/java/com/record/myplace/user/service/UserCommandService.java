package com.record.myplace.user.service;

import com.record.myplace.com.dto.MessageResponse;
import com.record.myplace.user.dto.UpdateProfileResponse;

public interface UserCommandService {

    UpdateProfileResponse updateMe(String email, String nickname, String bio);

    MessageResponse changePassword(String email, String currentPassword, String newPassword);
}