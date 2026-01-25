package com.record.myplace.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.user.entity.User;
import com.record.myplace.user.repository.UserRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    
    // 마이페이지 프로필 조회
    public Map<String, Object> getMe(String email) {
    	User user = userRepository.findByEmail(email)
    		.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    	
    	return Map.of(
    		"useremail", user.getEmail(),
    		"nickname", user.getNickname(),
    		"bio", user.getBio()
    	);
    }
    
    // 마이페이지 프로필 수정
    @Transactional
    public Map<String, Object> updateMe(String email, String nickname, String bio) {
    	User user = userRepository.findByEmail(email)
    		.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    	
    	if (nickname == null || nickname.isBlank()) {
    		throw new IllegalArgumentException("닉네임은 필수입니다.");
    	}
    	
    	user.setNickname(nickname);
    	user.setBio(bio);
    	
    	return Map.of(
    		"message", "프로필이 저장되었습니다.",
    		"useremail", user.getEmail(),
    		"nickname", user.getNickname(),
    		"bio", user.getBio()
    	);
    }
}
