package com.tripcut.core.aspect;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tripcut.core.annotation.Cacheable;

@Aspect
@Component
public class CacheableAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    public CacheableAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(cacheable)")
    public Object cacheAround(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        String key = generateCacheKey(joinPoint, cacheable);
        
        if (!cacheable.refresh()) {
            Object cachedValue = redisTemplate.opsForValue().get(key);
            if (cachedValue != null) {
                return cachedValue;
            }
        }

        Object result = joinPoint.proceed();
        redisTemplate.opsForValue().set(key, result, cacheable.ttl(), TimeUnit.SECONDS);
        
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