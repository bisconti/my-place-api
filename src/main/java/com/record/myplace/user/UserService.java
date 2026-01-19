package com.record.myplace.user;

import com.record.myplace.jwt.JwtTokenProvider;
import com.record.myplace.user.dto.LoginRequest;
import com.record.myplace.user.dto.LoginResponse;
import com.record.myplace.user.dto.SignUpRequest;
import com.record.myplace.user.dto.UserDto;
import com.record.myplace.user.entity.PasswordResetToken;
import com.record.myplace.user.repository.PasswordResetTokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;
    private final MailService mailService;
    
    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordBaseUrl;
    
    public LoginResponse login(LoginRequest request) {
        // 이메일로 사용자 찾기
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("이메일이 존재하지 않습니다.");
        }

        User user = userOptional.get();

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
             throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String jwtToken = tokenProvider.createToken(user);
        
        // 응답 DTO 생성 및 반환
        return LoginResponse.builder()
                .user(new UserDto(user)) // User 엔티티를 UserDto로 변환
                .token(jwtToken)         // 생성된 토큰 포함
                .message("로그인 성공")
                .build();
    }

	public boolean checkEmailDuplication(String email) {
		Optional<User> userInfo = userRepository.findByEmail(email);
		
		if (userInfo.isEmpty()) {
			return true;	
		} else {
			return false;
		}
		
	}

	public void signUp(SignUpRequest req) {
		// 비밀번호 암호화
		String encodePassword = passwordEncoder.encode(req.getPassword());
		
		// User entity 설정
		User user = User.builder()
				.email(req.getEmail())
				.password(encodePassword)
				.username(req.getUsername())
				.birthDate(LocalDate.parse(req.getBirthDate()))
				.gender(req.getGender())
				.build();
		
		userRepository.save(user);
		
	}
	
    public void sendPasswordResetEmail(String email) {
        // 1) 이메일이 실제로 존재하는지 확인 (단, 응답은 항상 동일하게)
        boolean exists = userRepository.existsByEmail(email);
        if (!exists) {
            return; // 아무 것도 하지 않음
        }

        // 2) 기존 토큰 삭제(선택) - 재발급시 이전 링크 무효화
        tokenRepository.deleteByEmail(email);

        // 3) 토큰 생성 + 저장 (예: 30분 만료)
        String token = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);

        tokenRepository.save(new PasswordResetToken(token, email, expiresAt));

        // 4) 프론트 링크 생성
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String resetLink = resetPasswordBaseUrl + "?token=" + encodedToken;

        // 5) 메일 발송
        String subject = "[MyPlace] 비밀번호 재설정 안내";
        String body = ""
                + "안녕하세요.\n\n"
                + "비밀번호 재설정을 요청하셨습니다.\n"
                + "아래 링크를 통해 비밀번호를 재설정해주세요.\n\n"
                + resetLink + "\n\n"
                + "이 링크는 30분 후 만료됩니다.\n"
                + "본인이 요청하지 않았다면 이 메일을 무시하셔도 됩니다.\n";

        mailService.sendText(email, subject, body);
    }

}
