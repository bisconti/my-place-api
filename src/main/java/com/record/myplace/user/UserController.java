package com.record.myplace.user;

import com.record.myplace.user.dto.FindPasswordRequest;
import com.record.myplace.user.dto.LoginRequest;
import com.record.myplace.user.dto.LoginResponse;
import com.record.myplace.user.dto.ResetPasswordRequest;
import com.record.myplace.user.dto.SignUpRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /auth/checkEmailDup
     */
    @PostMapping("/checkEmailDup")
    public ResponseEntity<?> checkEmailDuplication(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일 주소를 입력해주세요.");
        }

        boolean result = userService.checkEmailDuplication(email);

        if (result) {
            return ResponseEntity.ok(Map.of("message", "사용 가능한 이메일입니다."));
        }

        return ResponseEntity.status(409)
                .body(Map.of("message", "이미 사용 중인 이메일입니다."));
    }

    /**
     * POST /auth/signUp
     */
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Validated @RequestBody SignUpRequest req) {
        userService.signUp(req);
        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }

    /**
     * POST /auth/find-password
     */
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@Validated @RequestBody FindPasswordRequest req) {
        userService.sendPasswordResetEmail(req.getEmail());
        return ResponseEntity.ok(Map.of(
                "message", "입력하신 이메일로 비밀번호 재설정 안내 메일을 발송했습니다."
        ));
    }

    /**
     * POST /auth/reset-password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Validated @RequestBody ResetPasswordRequest req) {
        userService.resetPassword(req.getToken(), req.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "비밀번호가 변경되었습니다."));
    }

    /**
     * GET /auth/reset-password/validate
     */
    @GetMapping("/reset-password/validate")
    public ResponseEntity<?> validateResetPasswordToken(@RequestParam("token") String token) {
        userService.validateResetPasswordToken(token);
        return ResponseEntity.ok(Map.of("valid", true));
    }
}
