package com.example.wegobe.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    private static final long REFRESH_TOKEN_EXPIRATION_SECONDS = 7 * 24 * 60 * 60; // 7일

    // RefreshToken 저장
    public void saveRefreshToken(String key, String refreshToken) {
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofSeconds(REFRESH_TOKEN_EXPIRATION_SECONDS));
    }

    // RefreshToken 조회
    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // RefreshToken 삭제
    public void deleteRefreshToken(String key) {
        redisTemplate.delete(key);
    }

    // RefreshToken 유효성 검증
    public void validateRefreshToken(String key, String refreshToken) {
        String savedToken = getRefreshToken(key);
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 RefreshToken 입니다.");
        }
    }
}
