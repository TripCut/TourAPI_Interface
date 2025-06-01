package com.tripcut.global.security.detection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.tripcut.global.redis.service.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginPatternDetector {
    private final RedisService redisService;
    
    private static final String LOGIN_ATTEMPT_PREFIX = "login_attempt:";
    private static final String SUSPICIOUS_IP_PREFIX = "suspicious_ip:";
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int LOGIN_ATTEMPT_WINDOW_MINUTES = 15;
    private static final int SUSPICIOUS_IP_BLOCK_MINUTES = 60;
    
    private final Map<String, List<LoginAttempt>> loginAttemptCache = new ConcurrentHashMap<>();
    
    public boolean isSuspiciousLogin(String email, String ipAddress) {
        // IP 기반 의심스러운 활동 확인
        if (isIpBlocked(ipAddress)) {
            log.warn("Blocked IP attempt: {} for user: {}", ipAddress, email);
            return true;
        }
        
        // 로그인 시도 횟수 확인
        if (isExcessiveLoginAttempts(email, ipAddress)) {
            blockIp(ipAddress);
            log.warn("Excessive login attempts detected for IP: {} and user: {}", ipAddress, email);
            return true;
        }
        
        // 비정상적인 시간대 로그인 확인
        if (isAbnormalLoginTime()) {
            log.warn("Abnormal login time detected for user: {}", email);
            return true;
        }
        
        // 이전 로그인 위치와의 거리 확인
        if (isAbnormalLocationChange(email, ipAddress)) {
            log.warn("Abnormal location change detected for user: {}", email);
            return true;
        }
        
        return false;
    }
    
    public void recordLoginAttempt(String email, String ipAddress, boolean success) {
        String key = LOGIN_ATTEMPT_PREFIX + email + ":" + ipAddress;
        LoginAttempt attempt = new LoginAttempt(LocalDateTime.now(), success);
        
        List<LoginAttempt> attempts = loginAttemptCache.computeIfAbsent(key, k -> new ArrayList<>());
        attempts.add(attempt);
        
        // 오래된 시도 기록 제거
        attempts.removeIf(a -> a.timestamp.isBefore(LocalDateTime.now().minusMinutes(LOGIN_ATTEMPT_WINDOW_MINUTES)));
        
        // Redis에 기록
        redisService.addTokenHistory(email, ipAddress, success ? "LOGIN_SUCCESS" : "LOGIN_FAILURE");
    }
    
    private boolean isIpBlocked(String ipAddress) {
        String key = SUSPICIOUS_IP_PREFIX + ipAddress;
        return redisService.exists(key);
    }
    
    private void blockIp(String ipAddress) {
        String key = SUSPICIOUS_IP_PREFIX + ipAddress;
        redisService.set(key, "blocked", SUSPICIOUS_IP_BLOCK_MINUTES * 60);
    }
    
    private boolean isExcessiveLoginAttempts(String email, String ipAddress) {
        String key = LOGIN_ATTEMPT_PREFIX + email + ":" + ipAddress;
        List<LoginAttempt> attempts = loginAttemptCache.getOrDefault(key, new ArrayList<>());
        
        // 최근 실패한 시도 횟수 계산
        long recentFailures = attempts.stream()
                .filter(a -> !a.success)
                .filter(a -> a.timestamp.isAfter(LocalDateTime.now().minusMinutes(LOGIN_ATTEMPT_WINDOW_MINUTES)))
                .count();
        
        return recentFailures >= MAX_LOGIN_ATTEMPTS;
    }
    
    private boolean isAbnormalLoginTime() {
        int hour = LocalDateTime.now().getHour();
        // 일반적인 업무 시간 외 로그인 (예: 오전 2시 ~ 오전 6시)
        return hour >= 2 && hour < 6;
    }
    
    private boolean isAbnormalLocationChange(String email, String ipAddress) {
        // TODO: IP 기반 위치 정보를 사용하여 이전 로그인 위치와의 거리 계산
        // 현재는 간단한 구현을 위해 false 반환
        return false;
    }
    
    private static class LoginAttempt {
        private final LocalDateTime timestamp;
        private final boolean success;
        
        public LoginAttempt(LocalDateTime timestamp, boolean success) {
            this.timestamp = timestamp;
            this.success = success;
        }
    }
} 