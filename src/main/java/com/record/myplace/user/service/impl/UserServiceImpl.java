package com.record.myplace.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.com.dto.MessageResponse;
import com.record.myplace.user.dto.MeResponse;
import com.record.myplace.user.dto.UpdateProfileResponse;
import com.record.myplace.user.entity.User;
import com.record.myplace.user.repository.UserRepository;
import com.record.myplace.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

    // 마이페이지 프로필 조회
	@Override
	public MeResponse getMe(String email) {
	    User user = userRepository.findById(email)
	            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

	    return new MeResponse(
	            user.getEmail(),
	            user.getNickname(),
	            user.getBio()
	    );
	}
	
    // 마이페이지 프로필 수정
	@Override
	@Transactional
	public UpdateProfileResponse updateMe(String email, String nickname, String bio) {

	    User user = userRepository.findById(email)
	            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

	    user.setNickname(nickname);
	    user.setBio(bio);

	    return new UpdateProfileResponse(
	            "프로필이 저장되었습니다.",
	            user.getEmail(),
	            user.getNickname(),
	            user.getBio()
	    );
	}
    
	@Override
	@Transactional
	public MessageResponse changePassword(String email, String currentPassword, String newPassword) {

	    User user = userRepository.findById(email)
	            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

	    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
	        throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
	    }

	    user.setPassword(passwordEncoder.encode(newPassword));

	    return new MessageResponse("비밀번호가 변경되었습니다.");
	}

}
