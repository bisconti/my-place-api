package com.record.myplace.infra.redis;

import java.time.Duration;
import java.util.Collections;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisLockRepository {

    private static final String LOCK_KEY_PREFIX = "lock:";

    private final StringRedisTemplate redisTemplate;

    public boolean tryLock(String lockName, String owner, Duration ttl) {
        try {
            Boolean acquired = redisTemplate.opsForValue()
                    .setIfAbsent(getLockKey(lockName), owner, ttl);
            return Boolean.TRUE.equals(acquired);
        } catch (RuntimeException ex) {
            log.warn("Redis 락 획득 실패. lockName={}, fallback=run-without-lock", lockName, ex);
            return true;
        }
    }

    public void unlock(String lockName, String owner) {
        try {
            String script = ""
                    + "if redis.call('get', KEYS[1]) == ARGV[1] then "
                    + "return redis.call('del', KEYS[1]) "
                    + "else "
                    + "return 0 "
                    + "end";

            redisTemplate.execute(
                    new DefaultRedisScript<>(script, Long.class),
                    Collections.singletonList(getLockKey(lockName)),
                    owner
            );
        } catch (RuntimeException ex) {
            log.warn("Redis 락 해제 실패. lockName={}", lockName, ex);
        }
    }

    private String getLockKey(String lockName) {
        return LOCK_KEY_PREFIX + lockName;
    }
}
