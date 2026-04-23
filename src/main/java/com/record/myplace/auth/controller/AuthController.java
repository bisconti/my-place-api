package com.record.myplace.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.record.myplace.auth.dto.CheckEmailRequest;
import com.record.myplace.auth.dto.FindPasswordRequest;
import com.record.myplace.auth.dto.LoginRequest;
import com.record.myplace.auth.dto.LoginResponse;
import com.record.myplace.auth.dto.ResetPasswordRequest;
import com.record.myplace.auth.dto.SignUpRequest;
import com.record.myplace.auth.dto.TokenRefreshResponse;
import com.record.myplace.auth.dto.ValidateTokenResponse;
import com.record.myplace.auth.service.AuthService;
import com.record.myplace.com.dto.MessageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	
    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(
            @RequestBody Map<String, String> body) {

        String refreshToken = body.get("refreshToken");
        TokenRefreshResponse response = authService.refresh(refreshToken);

        return ResponseEntity.ok(response);
    }
    
    /*
     * 로그아웃 
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        authService.logout(email);
        return ResponseEntity.ok().build();
    }

    
    /**
     * 이메일 중복체크
     */
    @PostMapping("/checkEmailDup")
    public ResponseEntity<MessageResponse> checkEmailDuplication(@RequestBody CheckEmailRequest request) {
        String email = request.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일 주소를 입력해주세요.");
        }

        boolean available = authService.checkEmailDuplication(email);

        if (available) {
            return ResponseEntity.ok(new MessageResponse("사용 가능한 이메일입니다."));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("이미 사용 중인 이메일입니다."));
    }
    
    /**
     * 회원가입
     */
    @PostMapping("/signUp")
    public ResponseEntity<MessageResponse> signUp(@Validated @RequestBody SignUpRequest req) {
        authService.signUp(req);
        return ResponseEntity.ok(new MessageResponse("회원가입 성공"));
    }
    
    /**
     * 비밀번호 찾기(이메일 비밀번호 재설정 링크 발송)
     */
    @PostMapping("/find-password")
    public ResponseEntity<MessageResponse> findPassword(@Validated @RequestBody FindPasswordRequest req) {
        authService.sendPasswordResetEmail(req.getEmail());
        return ResponseEntity.ok(new MessageResponse("입력하신 이메일로 비밀번호 재설정 안내 메일을 발송했습니다."));
    }
    
    /**
     * 비밀번호 재설정
     */
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Validated @RequestBody ResetPasswordRequest req) {
        authService.resetPassword(req.getToken(), req.getNewPassword());
        return ResponseEntity.ok(new MessageResponse("비밀번호가 변경되었습니다."));
    }
    
    /**
     * 비밀번호 재설정 토큰 유효 검증
     */
    @GetMapping("/reset-password/validate")
    public ResponseEntity<ValidateTokenResponse> validateResetPasswordToken(@RequestParam("token") String token) {
        authService.validateResetPasswordToken(token);
        return ResponseEntity.ok(new ValidateTokenResponse(true));
    }
}
