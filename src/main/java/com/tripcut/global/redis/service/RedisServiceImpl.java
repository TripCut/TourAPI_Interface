package com.tripcut.global.redis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    private static final String SESSION_PREFIX = "session:";
    private static final String TOKEN_HISTORY_PREFIX = "token_history:";

    @Override
    public void set(String key, String value, long expirationSeconds) {
        redisTemplate.opsForValue().set(key, value, expirationSeconds, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void setUserSession(String email, String sessionId, long expirationSeconds) {
        String key = SESSION_PREFIX + email;
        set(key, sessionId, expirationSeconds);
    }

    @Override
    public void removeUserSession(String email) {
        String key = SESSION_PREFIX + email;
        delete(key);
    }

    @Override
    public boolean isUserLoggedIn(String email) {
        String key = SESSION_PREFIX + email;
        return exists(key);
    }

    @Override
    public String getUserSessionId(String email) {
        String key = SESSION_PREFIX + email;
        return get(key);
    }

    @Override
    public void addTokenHistory(String email, String token, String action) {
        String key = TOKEN_HISTORY_PREFIX + email;
        String value = String.format("{\"token\":\"%s\",\"action\":\"%s\",\"timestamp\":%d}", token, action, System.currentTimeMillis());
        redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public List<Map<String, String>> getTokenHistory(String email) {
        String key = TOKEN_HISTORY_PREFIX + email;
        List<Map<String, String>> result = new ArrayList<>();
        List<String> entries = redisTemplate.opsForList().range(key, 0, -1);
        if (entries != null) {
            for (String entry : entries) {
                Map<String, String> map = new HashMap<>();
                map.put("raw", entry);
                result.add(map);
            }
        }
        return result;
    }

    @Override
    public void clearTokenHistory(String email) {
        String key = TOKEN_HISTORY_PREFIX + email;
        redisTemplate.delete(key);
    }

    @Override
    public boolean isRedisAvailable() {
        try {
            redisTemplate.hasKey("health_check");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 