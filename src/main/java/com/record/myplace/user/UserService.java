package com.record.myplace.user;

import com.record.myplace.user.dto.LoginRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // 의존성 주입 (생성자 주입)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 로그인 비즈니스 로직
    @Transactional(readOnly = true)
    public User login(LoginRequest request) {
        // 1. 이메일로 사용자 찾기
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            // 이메일이 DB에 존재하지 않음
            throw new RuntimeException("이메일이 존재하지 않습니다.");
        }

        User user = userOptional.get();

        // 2. 비밀번호 일치 확인 (실제 운영 환경에서는 반드시 암호화된 비밀번호를 비교해야 합니다.)
        // 여기서는 임시 테스트를 위해 하드코딩된 "testpassword"와 비교합니다.
        if (!request.getPassword().equals(user.getPasswordHash())) {
             throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 성공
        return user;
    }
}
