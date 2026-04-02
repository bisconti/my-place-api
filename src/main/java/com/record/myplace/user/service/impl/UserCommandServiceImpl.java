package com.record.myplace.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.com.dto.MessageResponse;
import com.record.myplace.user.dto.UpdateProfileResponse;
import com.record.myplace.user.entity.User;
import com.record.myplace.user.repository.UserRepository;
import com.record.myplace.user.service.UserCommandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UpdateProfileResponse updateMe(String email, String nickname, String bio) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setNickname(nickname);
        user.setBio(bio);

        User savedUser = userRepository.save(user);

        UpdateProfileResponse response = new UpdateProfileResponse();
        response.setEmail(savedUser.getEmail());
        response.setNickname(savedUser.getNickname());
        response.setBio(savedUser.getBio());

        return response;
    }

    @Override
    public MessageResponse changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return new MessageResponse("비밀번호가 성공적으로 변경되었습니다.");
    }
}