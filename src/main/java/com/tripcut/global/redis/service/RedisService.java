package com.tripcut.global.redis.service;

import java.util.List;
import java.util.Map;

public interface RedisService {
    void set(String key, String value, long expirationSeconds);
    String get(String key);
    void delete(String key);
    boolean exists(String key);

    void setUserSession(String email, String sessionId, long expirationSeconds);
    void removeUserSession(String email);
    boolean isUserLoggedIn(String email);
    String getUserSessionId(String email);

    void addTokenHistory(String email, String token, String action);
    List<Map<String, String>> getTokenHistory(String email);
    void clearTokenHistory(String email);

    boolean isRedisAvailable();
} 