package com.tripcut.global.redis;

public interface RefreshTokenService {
    void saveRefreshToken(String email, String refreshToken, long expirationSeconds);
    String getRefreshToken(String email);
    void deleteRefreshToken(String email);
    boolean validateRefreshToken(String email, String refreshToken);
} 