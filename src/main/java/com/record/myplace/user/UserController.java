package com.record.myplace.user;

import com.record.myplace.user.dto.LoginRequest;
import com.record.myplace.user.dto.LoginResponse;
import com.record.myplace.user.dto.SignUpRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
        	LoginResponse response = userService.login(request);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }
    
    /**
     * POST /api/checkEmailDup
     * 이메일 중복 여부를 확인
     *
     * @param request 요청 본문 (예: {"email": "test@example.com"})
     * @return 중복이 아닐 경우 200 OK, 중복일 경우 409 Conflict와 오류 메시지
     */
    @PostMapping("/checkEmailDup")
    public ResponseEntity<?> checkEmailDuplication(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        // 1. 입력 유효성 검사
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일 주소를 입력해주세요."));
        }

        // 이메일 중복체크
        boolean result = userService.checkEmailDuplication(email);

        if (result) {
            return ResponseEntity.ok(Map.of("message", "사용 가능한 이메일입니다."));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "이미 사용 중인 이메일입니다."));
        }
    }
    
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Validated @RequestBody SignUpRequest req) {
    	userService.signUp(req);
    	
    	return ResponseEntity.ok().body("회원가입 성공");
    }
}
	 
