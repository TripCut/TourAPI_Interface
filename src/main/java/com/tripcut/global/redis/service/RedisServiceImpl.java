package com.tripcut.global.redis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    private boolean fallbackMode = false;
    private final Map<String, String> fallbackStore = new ConcurrentHashMap<>();
    private static final String SESSION_PREFIX = "session:";
    private static final String TOKEN_HISTORY_PREFIX = "token_history:";

    @Override
    public void set(String key, String value, long expirationSeconds) {
        try {
            if (!fallbackMode) {
                redisTemplate.opsForValue().set(key, value, expirationSeconds, java.util.concurrent.TimeUnit.SECONDS);
            } else {
                fallbackStore.put(key, value);
            }
        } catch (RedisConnectionFailureException e) {
            log.error("Redis connection failed, switching to fallback mode", e);
            setFallbackMode(true);
            fallbackStore.put(key, value);
        }
    }

    @Override
    public String get(String key) {
        try {
            if (!fallbackMode) {
                return redisTemplate.opsForValue().get(key);
            }
            return fallbackStore.get(key);
        } catch (RedisConnectionFailureException e) {
            log.error("Redis connection failed, switching to fallback mode", e);
            setFallbackMode(true);
            return fallbackStore.get(key);
        }
    }

    @Override
    public void delete(String key) {
        try {
            if (!fallbackMode) {
                redisTemplate.delete(key);
            } else {
                fallbackStore.remove(key);
            }
        } catch (RedisConnectionFailureException e) {
            log.error("Redis connection failed, switching to fallback mode", e);
            setFallbackMode(true);
            fallbackStore.remove(key);
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            if (!fallbackMode) {
                return Boolean.TRUE.equals(redisTemplate.hasKey(key));
            }
            return fallbackStore.containsKey(key);
        } catch (RedisConnectionFailureException e) {
            log.error("Redis connection failed, switching to fallback mode", e);
            setFallbackMode(true);
            return fallbackStore.containsKey(key);
        }
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
        try {
            if (!fallbackMode) {
                redisTemplate.opsForList().rightPush(key, value);
            } else {
                fallbackStore.put(key + ":" + System.currentTimeMillis(), value);
            }
        } catch (RedisConnectionFailureException e) {
            log.error("Redis connection failed, switching to fallback mode", e);
            setFallbackMode(true);
            fallbackStore.put(key + ":" + System.currentTimeMillis(), value);
        }
    }

    @Override
    public List<Map<String, String>> getTokenHistory(String email) {
        String key = TOKEN_HISTORY_PREFIX + email;
        List<Map<String, String>> result = new ArrayList<>();
        try {
            if (!fallbackMode) {
                List<String> entries = redisTemplate.opsForList().range(key, 0, -1);
                if (entries != null) {
                    for (String entry : entries) {
                        Map<String, String> map = new HashMap<>();
                        map.put("raw", entry);
                        result.add(map);
                    }
                }
            } else {
                fallbackStore.forEach((k, v) -> {
                    if (k.startsWith(key)) {
                        Map<String, String> map = new HashMap<>();
                        map.put("raw", v);
                        result.add(map);
                    }
                });
            }
        } catch (RedisConnectionFailureException e) {
            log.error("Redis connection failed, switching to fallback mode", e);
            setFallbackMode(true);
        }
        return result;
    }

    @Override
    public void clearTokenHistory(String email) {
        String key = TOKEN_HISTORY_PREFIX + email;
        try {
            if (!fallbackMode) {
                redisTemplate.delete(key);
            } else {
                fallbackStore.keySet().removeIf(k -> k.startsWith(key));
            }
        } catch (RedisConnectionFailureException e) {
            log.error("Redis connection failed, switching to fallback mode", e);
            setFallbackMode(true);
            fallbackStore.keySet().removeIf(k -> k.startsWith(key));
        }
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

    @Override
    public void setFallbackMode(boolean enabled) {
        this.fallbackMode = enabled;
    }

    @Override
    public boolean isInFallbackMode() {
        return fallbackMode;
    }
} 