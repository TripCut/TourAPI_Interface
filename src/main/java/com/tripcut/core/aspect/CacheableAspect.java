package com.tripcut.core.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tripcut.core.annotation.Cacheable;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.data.redis.enabled", havingValue = "true")
public class CacheableAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(cacheable)")
    public Object cache(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        String cacheKey = generateCacheKey(joinPoint, cacheable);
        
        // 캐시에서 조회
        Object cachedResult = redisTemplate.opsForValue().get(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }
        
        // 메서드 실행
        Object result = joinPoint.proceed();
        
        // 결과를 캐시에 저장
        if (result != null) {
            redisTemplate.opsForValue().set(cacheKey, result, cacheable.ttl(), java.util.concurrent.TimeUnit.SECONDS);
        }
        
        return result;
    }

    private String generateCacheKey(ProceedingJoinPoint joinPoint, Cacheable cacheable) {
        if (StringUtils.hasText(cacheable.key())) {
            return cacheable.key();
        }

        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        return methodName + ":" + Arrays.toString(args);
    }
} 