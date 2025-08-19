package com.tripcut.global.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    @Override
    public void saveRefreshToken(String email, String refreshToken, long expirationSeconds) {
        String key = REFRESH_TOKEN_PREFIX + email;
        redisTemplate.opsForValue().set(key, refreshToken, expirationSeconds, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public String getRefreshToken(String email) {
        String key = REFRESH_TOKEN_PREFIX + email;
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteRefreshToken(String email) {
        String key = REFRESH_TOKEN_PREFIX + email;
        redisTemplate.delete(key);
    }

    @Override
    public boolean validateRefreshToken(String email, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + email;
        String stored = redisTemplate.opsForValue().get(key);
        return refreshToken.equals(stored);
    }
} 