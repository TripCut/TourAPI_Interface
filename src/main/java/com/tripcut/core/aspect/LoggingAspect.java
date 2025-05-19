package com.tripcut.core.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.tripcut.core.annotation.Logging;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("@annotation(logging)")
    public Object logAround(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        
        if (logging.includeArgs()) {
            log.info("Method [{}] called with parameters: {}", methodName, joinPoint.getArgs());
        }

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        if (logging.includeResult()) {
            log.info("Method [{}] completed in {}ms with result: {}", 
                    methodName, (endTime - startTime), result);
        }

        return result;
    }
} 