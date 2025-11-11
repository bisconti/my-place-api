package com.record.myplace.user;

import com.record.myplace.jwt.JwtTokenProvider;
import com.record.myplace.user.dto.LoginRequest;
import com.record.myplace.user.dto.LoginResponse; // 새로 임포트
import com.record.myplace.user.dto.UserDto; // 새로 임포트
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider; // 1. JwtTokenProvider 주입

    // 의존성 주입 (생성자 주입)
    public UserService(UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    // 로그인 비즈니스 로직을 수정하여 LoginResponse를 반환합니다.
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) { // 반환 타입을 LoginResponse로 변경
        // 1. 이메일로 사용자 찾기
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("이메일이 존재하지 않습니다.");
        }

        User user = userOptional.get();

        // 2. 비밀번호 일치 확인 (실제 운영 환경에서는 반드시 암호화된 비밀번호를 비교해야 합니다.)
        // User 엔티티에 `getPasswordHash()` 대신 평문 비밀번호 필드가 있다면 `getPassword()`로 변경해야 합니다.
        if (!request.getPassword().equals(user.getPassword())) {
             throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 성공 후 JWT 토큰 생성
        String jwtToken = tokenProvider.createToken(user);
        
        // 4. 응답 DTO 생성 및 반환
        return LoginResponse.builder()
                .user(new UserDto(user)) // User 엔티티를 UserDto로 변환
                .token(jwtToken)         // 생성된 토큰 포함
                .message("로그인 성공")
                .build();
    }
}
