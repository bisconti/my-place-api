package com.record.myplace.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.record.myplace.user.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // 마이페이지 프로필 수정 조회
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
    	return ResponseEntity.ok(userService.getMe(auth.getName()));
    }
    
    // 마이페이지 프로필 수정 업데이트
    @PutMapping("/me")
    public ResponseEntity<?> updateMe(@RequestBody Map<String, String> req, Authentication auth) {
    	String nickname = req.get("nickname");
    	String bio = req.get("bio");
    	return ResponseEntity.ok(userService.updateMe(auth.getName(), nickname, bio));
    }
}
