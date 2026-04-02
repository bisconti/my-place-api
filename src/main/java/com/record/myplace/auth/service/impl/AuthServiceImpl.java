package com.record.myplace.auth.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.record.myplace.auth.dto.AuthUserQueryDto;
import com.record.myplace.auth.dto.LoginRequest;
import com.record.myplace.auth.dto.LoginResponse;
import com.record.myplace.auth.dto.PasswordResetTokenQueryDto;
import com.record.myplace.auth.dto.SignUpRequest;
import com.record.myplace.auth.entity.PasswordResetToken;
import com.record.myplace.auth.entity.RefreshToken;
import com.record.myplace.auth.exception.UnauthorizedException;
import com.record.myplace.auth.mapper.AuthQueryMapper;
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
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthQueryMapper authQueryMapper;
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
        AuthUserQueryDto userInfo = authQueryMapper.selectUserByEmail(request.getEmail());

        if (userInfo == null) {
            throw new UnauthorizedException("이메일 또는 비밀번호를 확인하세요.");
        }

        if (!passwordEncoder.matches(request.getPassword(), userInfo.getPassword())) {
            throw new UnauthorizedException("이메일 또는 비밀번호를 확인하세요.");
        }

        User user = User.builder()
                .email(userInfo.getEmail())
                .password(userInfo.getPassword())
                .username(userInfo.getUsername())
                .nickname(userInfo.getNickname())
                .birthDate(userInfo.getBirthDate())
                .gender(userInfo.getGender())
                .bio(userInfo.getBio())
                .build();

        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken(user.getEmail());

        RefreshToken rt = new RefreshToken();
        rt.setUseremail(user.getEmail());
        rt.setTokenHash(tokenHashUtil.sha256(refreshToken));
        rt.setExpiresAt(LocalDateTime.now().plusDays(14));
        refreshTokenRepository.save(rt);

        return LoginResponse.builder()
                .user(new UserDto(user))
                .token(accessToken)
                .refreshToken(refreshToken)
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

        AuthUserQueryDto userInfo = authQueryMapper.selectUserByEmail(email);
        if (userInfo == null) {
            throw new UnauthorizedException("사용자 없음");
        }

        User user = User.builder()
                .email(userInfo.getEmail())
                .password(userInfo.getPassword())
                .username(userInfo.getUsername())
                .nickname(userInfo.getNickname())
                .birthDate(userInfo.getBirthDate())
                .gender(userInfo.getGender())
                .bio(userInfo.getBio())
                .build();

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
        Integer count = authQueryMapper.selectUserCountByEmail(email);
        return count == null || count == 0;
    }

    @Override
    @Transactional
    public void signUp(SignUpRequest req) {
        String encodePassword = passwordEncoder.encode(req.getPassword());

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

        Integer count = authQueryMapper.selectUserCountByEmail(email);
        boolean exists = count != null && count > 0;

        log.info("[find-password] email exists? {}", exists);

        if (!exists) {
            return;
        }

        tokenRepository.deleteByEmail(email);

        String token = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);

        tokenRepository.save(new PasswordResetToken(token, email, expiresAt));

        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String resetLink = resetPasswordBaseUrl + "?token=" + encodedToken;

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
        PasswordResetTokenQueryDto tokenInfo = authQueryMapper.selectPasswordResetTokenByToken(token);

        if (tokenInfo == null) {
            throw new IllegalArgumentException("재설정 링크가 유효하지 않습니다.");
        }

        if (Boolean.TRUE.equals(tokenInfo.getUsed())) {
            throw new IllegalArgumentException("이미 사용된 재설정 링크입니다.");
        }

        if (tokenInfo.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("재설정 링크가 만료되었습니다.");
        }

        User user = authRepository.findByEmail(tokenInfo.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        String encoded = passwordEncoder.encode(newPassword);
        user.setPassword(encoded);

        authRepository.save(user);

        PasswordResetToken prt = tokenRepository.findById(tokenInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("재설정 토큰을 찾을 수 없습니다."));

        prt.markUsed();
        tokenRepository.save(prt);

        log.info("[reset-password] password updated. email={}", tokenInfo.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public void validateResetPasswordToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("재설정 링크가 유효하지 않습니다.");
        }

        PasswordResetTokenQueryDto tokenInfo = authQueryMapper.selectPasswordResetTokenByToken(token);

        if (tokenInfo == null) {
            throw new IllegalArgumentException("재설정 링크가 유효하지 않습니다.");
        }

        if (Boolean.TRUE.equals(tokenInfo.getUsed())) {
            throw new IllegalArgumentException("이미 사용된 재설정 링크입니다.");
        }

        if (tokenInfo.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("재설정 링크가 만료되었습니다.");
        }
    }
}