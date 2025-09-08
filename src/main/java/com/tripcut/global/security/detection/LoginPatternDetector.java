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
    private static final String LAST_LOGIN_LOCATION_PREFIX = "last_login_location:";
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int LOGIN_ATTEMPT_WINDOW_MINUTES = 15;
    private static final int SUSPICIOUS_IP_BLOCK_MINUTES = 60;
    private static final long LAST_LOGIN_LOCATION_TTL_SECONDS = 60L * 60 * 24 * 30; // 30 days
    
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

        // 마지막 로그인 위치 저장 (성공한 로그인에 한해서)
        if (success) {
            double[] location = getLocationFromIp(ipAddress);
            String locationKey = LAST_LOGIN_LOCATION_PREFIX + email;
            String locationValue = location[0] + "," + location[1];
            redisService.set(locationKey, locationValue, LAST_LOGIN_LOCATION_TTL_SECONDS);
        }
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
        String locationKey = LAST_LOGIN_LOCATION_PREFIX + email;
        String lastLocation = redisService.get(locationKey);

        double[] currentLocation = getLocationFromIp(ipAddress);
        String currentValue = currentLocation[0] + "," + currentLocation[1];

        if (lastLocation == null) {
            // 첫 로그인 시 위치 저장 후 정상으로 처리
            redisService.set(locationKey, currentValue, LAST_LOGIN_LOCATION_TTL_SECONDS);
            return false;
        }

        String[] parts = lastLocation.split(",");
        if (parts.length != 2) {
            redisService.set(locationKey, currentValue, LAST_LOGIN_LOCATION_TTL_SECONDS);
            return false;
        }

        try {
            double lastLat = Double.parseDouble(parts[0]);
            double lastLon = Double.parseDouble(parts[1]);
            double distance = calculateDistance(lastLat, lastLon, currentLocation[0], currentLocation[1]);

            // 현재 위치를 다음 비교를 위해 저장
            redisService.set(locationKey, currentValue, LAST_LOGIN_LOCATION_TTL_SECONDS);

            // 예시로 500km 이상 이동 시 비정상으로 판단
            return distance > 500;
        } catch (NumberFormatException e) {
            redisService.set(locationKey, currentValue, LAST_LOGIN_LOCATION_TTL_SECONDS);
            return false;
        }
    }

    private double[] getLocationFromIp(String ipAddress) {
        String[] segments = ipAddress.split("\\.");
        if (segments.length >= 2) {
            try {
                double lat = Double.parseDouble(segments[0]);
                double lon = Double.parseDouble(segments[1]);
                return new double[]{lat, lon};
            } catch (NumberFormatException ignored) {
            }
        }
        return new double[]{0.0, 0.0};
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
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