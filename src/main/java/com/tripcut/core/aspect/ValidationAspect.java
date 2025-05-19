package com.tripcut.core.aspect;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tripcut.core.annotation.Validation;

@Aspect
@Component
public class ValidationAspect {

    @Around("@annotation(validation)")
    public Object validateAround(ProceedingJoinPoint joinPoint, Validation validation) throws Throwable {
        Object[] args = joinPoint.getArgs();
        
        for (Object arg : args) {
            if (arg == null && validation.validateNull()) {
                throw new IllegalArgumentException("Null parameter is not allowed");
            }

            if (arg instanceof String && validation.validateEmpty() && !StringUtils.hasText((String) arg)) {
                throw new IllegalArgumentException("Empty string is not allowed");
            }

            if (arg != null && validation.requiredFields().length > 0) {
                validateRequiredFields(arg, validation.requiredFields());
            }
        }

        return joinPoint.proceed();
    }

    private void validateRequiredFields(Object obj, String[] requiredFields) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        for (String fieldName : requiredFields) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(obj);
                
                if (value == null) {
                    throw new IllegalArgumentException("Required field '" + fieldName + "' is null");
                }
                
                if (value instanceof String && !StringUtils.hasText((String) value)) {
                    throw new IllegalArgumentException("Required field '" + fieldName + "' is empty");
                }
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("Required field '" + fieldName + "' not found in " + clazz.getName());
            }
        }
    }
} 