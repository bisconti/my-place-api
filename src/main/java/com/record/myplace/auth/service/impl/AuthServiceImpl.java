package com.record.myplace.auth.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.auth.dto.LoginRequest;
import com.record.myplace.auth.dto.LoginResponse;
import com.record.myplace.auth.dto.SignUpRequest;
import com.record.myplace.auth.entity.PasswordResetToken;
import com.record.myplace.auth.entity.RefreshToken;
import com.record.myplace.auth.exception.UnauthorizedException;
import com.record.myplace.auth.repository.AuthRepository;
import com.record.myplace.auth.repository.PasswordResetTokenRepository;
import com.record.myplace.auth.repository.RefreshTokenRepository;
import com.record.myplace.auth.security.JwtTokenProvider;
import com.record.myplace.auth.service.AuthService;
import com.record.myplace.auth.util.TokenHashUtil;
import com.record.myplace.infra.mail.MailService;
import com.record.myplace.user.dto.UserDto;
import com.record.myplace.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

	private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final PasswordResetTokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHashUtil tokenHashUtil;
    private final MailService mailService;
    
    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordBaseUrl;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = authRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호를 확인하세요."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("이메일 또는 비밀번호를 확인하세요.");
        }

        // access token
        String accessToken = tokenProvider.createAccessToken(user);

        // refresh token
        String refreshToken = tokenProvider.createRefreshToken(user.getEmail());

        RefreshToken rt = new RefreshToken();
        rt.setUseremail(user.getEmail());
        rt.setTokenHash(tokenHashUtil.sha256(refreshToken));
        rt.setExpiresAt(LocalDateTime.now().plusDays(14));
        refreshTokenRepository.save(rt);

        return LoginResponse.builder()
            .user(new UserDto(user))
            .token(accessToken)
            .refreshToken(refreshToken) // ⭐ DTO에 추가
            .message("로그인 성공")
            .build();
    }
    
    @Override
    @Transactional
    public String refresh(String refreshTokenRaw) {
    	
    	if (!tokenProvider.validateToken(refreshTokenRaw)
    			|| !tokenProvider.isRefreshToken(refreshTokenRaw)) {
    		throw new UnauthorizedException("유효하지 않은 refresh token");
    	}
    	
    	String email = tokenProvider.getEmail(refreshTokenRaw);
    	String hash = tokenHashUtil.sha256(refreshTokenRaw);
    	
    	RefreshToken stored = refreshTokenRepository
    		.findByTokenHashAndRevoked(hash, "N")
    		.orElseThrow(() -> new UnauthorizedException("refresh token 만료"));
    	
    	if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
    		stored.setRevoked("Y");
    		throw new UnauthorizedException("refresh token 만료");
    	}
    	
    	stored.setRevoked("Y");
    	
    	User user = authRepository.findByEmail(email)
    		.orElseThrow(() -> new UnauthorizedException("사용자 없음"));
    	
        String newAccessToken = tokenProvider.createAccessToken(user);
        String newRefreshToken = tokenProvider.createRefreshToken(email);

        RefreshToken newRt = new RefreshToken();
        newRt.setUseremail(email);
        newRt.setTokenHash(tokenHashUtil.sha256(newRefreshToken));
        newRt.setExpiresAt(LocalDateTime.now().plusDays(14));
        refreshTokenRepository.save(newRt);

        return newAccessToken;
    }


	@Override
	public boolean checkEmailDuplication(String email) {
		Optional<User> userInfo = authRepository.findByEmail(email);
		
		if (userInfo.isEmpty()) {
			return true;	
		} else {
			return false;
		}
	}

	@Override
	public void signUp(SignUpRequest req) {
		// 비밀번호 암호화
		String encodePassword = passwordEncoder.encode(req.getPassword());
		
		// User entity 설정
		User user = User.builder()
				.email(req.getEmail())
				.password(encodePassword)
				.username(req.getUsername())
				.nickname(req.getNickname())
				.birthDate(LocalDate.parse(req.getBirthDate()))
				.gender(req.getGender())
				.bio(req.getBio())
				.build();
		
		authRepository.save(user);
	}

	@Override
	@Transactional
	public void sendPasswordResetEmail(String email) {
    	log.info("[find-password] requested email={}", email);
        // 1) 이메일이 실제로 존재하는지 확인
        boolean exists = authRepository.existsByEmail(email);
        log.info("[find-password] email exists? {}", exists);
        if (!exists) {
            return;
        }

        // 2) 기존 토큰 삭제, 재발급시 이전 링크 무효화
        tokenRepository.deleteByEmail(email);

        // 3) 토큰 생성 및 저장
        String token = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);

        tokenRepository.save(new PasswordResetToken(token, email, expiresAt));

        // 4) 링크 생성
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String resetLink = resetPasswordBaseUrl + "?token=" + encodedToken;

        // 5) 메일 발송
        String subject = "[MyPlace] 비밀번호 재설정 안내";
        String body = ""
                + "안녕하세요.\n\n"
                + "비밀번호 재설정을 요청하셨습니다.\n"
                + "아래 링크를 통해 비밀번호를 재설정해주세요.\n\n"
                + resetLink + "\n\n"
                + "이 링크는 30분 후 만료됩니다.\n";

        mailService.sendText(email, subject, body);
	}

	@Override
	@Transactional
	public void resetPassword(String token, String newPassword) {
		PasswordResetToken prt = tokenRepository.findByToken(token)
			.orElseThrow(() -> new IllegalArgumentException("재설정 링크가 유효하지 않습니다."));

		if (prt.isUsed()) {
			throw new IllegalArgumentException("이미 사용된 재설정 링크입니다.");
		}

		if (prt.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("재설정 링크가 만료되었습니다.");
		}

		// 토큰에 저장해둔 이메일로 사용자 찾기
		User user = authRepository.findByEmail(prt.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

		// BCrypt 암호화
		String encoded = passwordEncoder.encode(newPassword);
		user.setPassword(encoded);

		authRepository.save(user);

		// 토큰 사용 처리
		prt.markUsed();
		tokenRepository.save(prt);

		log.info("[reset-password] password updated. email={}", prt.getEmail());
	}

	@Override
	@Transactional(readOnly = true)
	// 비밀번호 재설정 토큰 검증
	public void validateResetPasswordToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("재설정 링크가 유효하지 않습니다.");
        }

        PasswordResetToken prt = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("재설정 링크가 유효하지 않습니다."));

        if (prt.isUsed()) {
            throw new IllegalArgumentException("이미 사용된 재설정 링크입니다.");
        }

        if (prt.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("재설정 링크가 만료되었습니다.");
        }
	}

}
