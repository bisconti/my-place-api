package com.record.myplace.auth.repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthRedisRepository {

    private static final String REFRESH_TOKEN_KEY_PREFIX = "auth:refresh:";
    private static final String USER_REFRESH_TOKEN_SET_KEY_PREFIX = "auth:refresh:user:";
    private static final String PASSWORD_RESET_TOKEN_KEY_PREFIX = "auth:password-reset:";
    private static final String PASSWORD_RESET_EMAIL_KEY_PREFIX = "auth:password-reset:email:";

    private final StringRedisTemplate redisTemplate;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMs;

    @Value("${auth.password-reset-expiration:1800000}")
    private long passwordResetExpirationMs;

    public void saveRefreshToken(String email, String tokenHash) {
        Duration ttl = Duration.ofMillis(refreshExpirationMs);

        redisTemplate.opsForValue().set(getRefreshTokenKey(tokenHash), email, ttl);
        redisTemplate.opsForSet().add(getUserRefreshTokenSetKey(email), tokenHash);
        redisTemplate.expire(getUserRefreshTokenSetKey(email), ttl);
    }

    public Optional<String> findRefreshTokenOwner(String tokenHash) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(getRefreshTokenKey(tokenHash)));
    }

    public void deleteRefreshToken(String tokenHash) {
        String refreshTokenKey = getRefreshTokenKey(tokenHash);
        String email = redisTemplate.opsForValue().get(refreshTokenKey);

        redisTemplate.delete(refreshTokenKey);

        if (StringUtils.hasText(email)) {
            redisTemplate.opsForSet().remove(getUserRefreshTokenSetKey(email), tokenHash);
        }
    }

    public void deleteAllRefreshTokens(String email) {
        String userRefreshTokenSetKey = getUserRefreshTokenSetKey(email);
        Set<String> tokenHashes = redisTemplate.opsForSet().members(userRefreshTokenSetKey);

        List<String> keysToDelete = new ArrayList<>();
        keysToDelete.add(userRefreshTokenSetKey);

        if (tokenHashes != null && !tokenHashes.isEmpty()) {
            tokenHashes.stream()
                    .map(this::getRefreshTokenKey)
                    .forEach(keysToDelete::add);
        }

        redisTemplate.delete(keysToDelete);
    }

    public void savePasswordResetToken(String email, String token) {
        Duration ttl = Duration.ofMillis(passwordResetExpirationMs);

        deletePasswordResetTokenByEmail(email);
        redisTemplate.opsForValue().set(getPasswordResetTokenKey(token), email, ttl);
        redisTemplate.opsForValue().set(getPasswordResetEmailKey(email), token, ttl);
    }

    public Optional<String> findPasswordResetEmail(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(getPasswordResetTokenKey(token)));
    }

    public void deletePasswordResetToken(String token) {
        String passwordResetTokenKey = getPasswordResetTokenKey(token);
        String email = redisTemplate.opsForValue().get(passwordResetTokenKey);

        redisTemplate.delete(passwordResetTokenKey);

        if (StringUtils.hasText(email)) {
            redisTemplate.delete(getPasswordResetEmailKey(email));
        }
    }

    public void deletePasswordResetTokenByEmail(String email) {
        String passwordResetEmailKey = getPasswordResetEmailKey(email);
        String token = redisTemplate.opsForValue().get(passwordResetEmailKey);

        redisTemplate.delete(passwordResetEmailKey);

        if (StringUtils.hasText(token)) {
            redisTemplate.delete(getPasswordResetTokenKey(token));
        }
    }

    private String getRefreshTokenKey(String tokenHash) {
        return REFRESH_TOKEN_KEY_PREFIX + tokenHash;
    }

    private String getUserRefreshTokenSetKey(String email) {
        return USER_REFRESH_TOKEN_SET_KEY_PREFIX + email;
    }

    private String getPasswordResetTokenKey(String token) {
        return PASSWORD_RESET_TOKEN_KEY_PREFIX + token;
    }

    private String getPasswordResetEmailKey(String email) {
        return PASSWORD_RESET_EMAIL_KEY_PREFIX + email;
    }
}
