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

    @Around("@annotation(com.tripcut.core.annotation.Cacheable)")
    public Object cache(ProceedingJoinPoint joinPoint) throws Throwable {
        // Redis가 비활성화된 경우 메서드를 직접 실행
        return joinPoint.proceed();
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