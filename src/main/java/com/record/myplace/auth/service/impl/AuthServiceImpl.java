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
import com.record.myplace.auth.dto.TokenRefreshResponse;
import com.record.myplace.auth.entity.PasswordResetToken;
import com.record.myplace.auth.entity.RefreshToken;
import com.record.myplace.auth.exception.UnauthorizedException;
import com.record.myplace.auth.mapper.AuthQueryMapper;
import com.record.myplace.auth.repository.AuthRedisRepository;
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
    private final AuthRedisRepository authRedisRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final TokenHashUtil tokenHashUtil;
    private final MailService mailService;

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordBaseUrl;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        AuthUserQueryDto userInfo = authQueryMapper.selectUserByEmail(request.getEmail());

        if (userInfo == null || !passwordEncoder.matches(request.getPassword(), userInfo.getPassword())) {
            throw new UnauthorizedException("이메일 또는 비밀번호를 확인해주세요.");
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
        String refreshTokenHash = tokenHashUtil.sha256(refreshToken);

        saveRefreshToken(user.getEmail(), refreshTokenHash);

        return LoginResponse.builder()
                .user(new UserDto(user))
                .token(accessToken)
                .refreshToken(refreshToken)
                .message("로그인에 성공했습니다.")
                .build();
    }

    @Override
    @Transactional
    public TokenRefreshResponse refresh(String refreshTokenRaw) {
        if (!tokenProvider.validateToken(refreshTokenRaw) || !tokenProvider.isRefreshToken(refreshTokenRaw)) {
            throw new UnauthorizedException("유효하지 않은 refresh token입니다.");
        }

        String email = tokenProvider.getEmail(refreshTokenRaw);
        String refreshTokenHash = tokenHashUtil.sha256(refreshTokenRaw);

        String storedEmail = findRefreshTokenOwner(refreshTokenHash)
                .orElseThrow(() -> new UnauthorizedException("refresh token이 만료되었거나 로그아웃되었습니다."));

        if (!email.equals(storedEmail)) {
            deleteRefreshToken(refreshTokenHash);
            throw new UnauthorizedException("refresh token 정보가 일치하지 않습니다.");
        }

        deleteRefreshToken(refreshTokenHash);

        AuthUserQueryDto userInfo = authQueryMapper.selectUserByEmail(email);
        if (userInfo == null) {
            throw new UnauthorizedException("사용자를 찾을 수 없습니다.");
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
        String newRefreshTokenHash = tokenHashUtil.sha256(newRefreshToken);

        saveRefreshToken(email, newRefreshTokenHash);

        return TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public boolean checkEmailDuplication(String email) {
        Integer count = authQueryMapper.selectUserCountByEmail(email);
        return count == null || count == 0;
    }

    @Override
    @Transactional
    public void signUp(SignUpRequest req) {
        String encodedPassword = passwordEncoder.encode(req.getPassword());

        User user = User.builder()
                .email(req.getEmail())
                .password(encodedPassword)
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

        String token = UUID.randomUUID().toString().replace("-", "");
        savePasswordResetToken(email, token);

        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String resetLink = resetPasswordBaseUrl + "?token=" + encodedToken;

        String subject = "[MyPlace] 비밀번호 재설정 안내";
        String body = ""
                + "안녕하세요.\n\n"
                + "비밀번호 재설정을 요청하셨습니다.\n"
                + "아래 링크를 통해 비밀번호를 재설정해주세요.\n\n"
                + resetLink + "\n\n"
                + "이 링크는 30분 뒤에 만료됩니다.\n";

        mailService.sendText(email, subject, body);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        String email = findPasswordResetEmail(token)
                .orElseThrow(() -> new IllegalArgumentException("재설정 링크가 유효하지 않거나 만료되었습니다."));

        User user = authRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(newPassword));
        authRepository.save(user);

        deletePasswordResetToken(token);

        log.info("[reset-password] password updated. email={}", email);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateResetPasswordToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("재설정 링크가 유효하지 않습니다.");
        }

        if (findPasswordResetEmail(token).isEmpty()) {
            throw new IllegalArgumentException("재설정 링크가 유효하지 않거나 만료되었습니다.");
        }
    }

    @Override
    @Transactional
    public void logout(String email) {
        if (email == null || email.isBlank()) {
            return;
        }

        deleteAllRefreshTokens(email);
    }

    private void saveRefreshToken(String email, String refreshTokenHash) {
        saveRefreshTokenToDatabase(email, refreshTokenHash);

        try {
            authRedisRepository.saveRefreshToken(email, refreshTokenHash);
        } catch (RuntimeException ex) {
            log.warn("Redis refresh token 저장 실패. email={}, fallback=DB", email, ex);
        }
    }

    private java.util.Optional<String> findRefreshTokenOwner(String refreshTokenHash) {
        try {
            java.util.Optional<String> redisOwner = authRedisRepository.findRefreshTokenOwner(refreshTokenHash);
            if (redisOwner.isPresent()) {
                return redisOwner;
            }
        } catch (RuntimeException ex) {
            log.warn("Redis refresh token 조회 실패. fallback=DB", ex);
        }

        return refreshTokenRepository.findByTokenHashAndRevoked(refreshTokenHash, "N")
                .filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(RefreshToken::getUseremail);
    }

    private void deleteRefreshToken(String refreshTokenHash) {
        refreshTokenRepository.findByTokenHashAndRevoked(refreshTokenHash, "N")
                .ifPresent(token -> token.setRevoked("Y"));

        try {
            authRedisRepository.deleteRefreshToken(refreshTokenHash);
        } catch (RuntimeException ex) {
            log.warn("Redis refresh token 삭제 실패. fallback=DB", ex);
        }
    }

    private void deleteAllRefreshTokens(String email) {
        refreshTokenRepository.findAllByUseremailAndRevoked(email, "N")
                .forEach(token -> token.setRevoked("Y"));

        try {
            authRedisRepository.deleteAllRefreshTokens(email);
        } catch (RuntimeException ex) {
            log.warn("Redis refresh token 일괄 삭제 실패. fallback=DB", ex);
        }
    }

    private void saveRefreshTokenToDatabase(String email, String refreshTokenHash) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUseremail(email);
        refreshToken.setTokenHash(refreshTokenHash);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(14));
        refreshTokenRepository.save(refreshToken);
    }

    private void savePasswordResetToken(String email, String token) {
        passwordResetTokenRepository.deleteByEmail(email);
        passwordResetTokenRepository.save(
                new PasswordResetToken(token, email, LocalDateTime.now().plusMinutes(30))
        );

        try {
            authRedisRepository.savePasswordResetToken(email, token);
        } catch (RuntimeException ex) {
            log.warn("Redis password reset token 저장 실패. email={}, fallback=DB", email, ex);
        }
    }

    private java.util.Optional<String> findPasswordResetEmail(String token) {
        try {
            java.util.Optional<String> redisEmail = authRedisRepository.findPasswordResetEmail(token);
            if (redisEmail.isPresent()) {
                return redisEmail;
            }
        } catch (RuntimeException ex) {
            log.warn("Redis password reset token 조회 실패. fallback=DB", ex);
        }

        PasswordResetTokenQueryDto tokenInfo = authQueryMapper.selectPasswordResetTokenByToken(token);
        if (tokenInfo == null || Boolean.TRUE.equals(tokenInfo.getUsed())
                || tokenInfo.getExpiresAt().isBefore(LocalDateTime.now())) {
            return java.util.Optional.empty();
        }

        return java.util.Optional.ofNullable(tokenInfo.getEmail());
    }

    private void deletePasswordResetToken(String token) {
        passwordResetTokenRepository.findByToken(token)
                .ifPresent(entity -> {
                    entity.markUsed();
                    passwordResetTokenRepository.save(entity);
                });

        try {
            authRedisRepository.deletePasswordResetToken(token);
        } catch (RuntimeException ex) {
            log.warn("Redis password reset token 삭제 실패. fallback=DB", ex);
        }
    }
}
