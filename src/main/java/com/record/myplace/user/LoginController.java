package com.record.myplace.user;

import com.record.myplace.user.dto.LoginRequest;
import com.record.myplace.user.dto.LoginResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // 이 클래스가 REST API를 처리하는 컨트롤러임을 선언
@RequestMapping("/api") // 이 컨트롤러의 기본 경로를 /api로 설정
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // POST 요청 /api/login 을 처리합니다.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 로그인 서비스 호출
        	LoginResponse response = userService.login(request);

            // 로그인 성공 시 200 OK와 사용자 정보를 JSON 형태로 반환
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // 로그인 실패 시 401 Unauthorized (인증 실패) 상태 코드 반환
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }
}
