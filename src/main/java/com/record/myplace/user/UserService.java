package com.record.myplace.user;

import com.record.myplace.jwt.JwtTokenProvider;
import com.record.myplace.user.dto.LoginRequest;
import com.record.myplace.user.dto.LoginResponse;
import com.record.myplace.user.dto.SignUpRequest;
import com.record.myplace.user.dto.UserDto;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    
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
}
