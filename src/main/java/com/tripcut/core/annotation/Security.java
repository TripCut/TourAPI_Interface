package com.tripcut.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "보안 설정을 위한 어노테이션",
    examples = {
        "@Security(roles = {\"ADMIN\", \"USER\"}, requireAuth = true)",
        "@Security(permissions = {\"READ\", \"WRITE\"}, checkIp = true)"
    },
    notes = {
        "메서드나 API의 보안 설정을 관리합니다.",
        "roles: 접근 가능한 역할 목록",
        "permissions: 필요한 권한 목록",
        "requireAuth: 인증 필요 여부 (기본값: true)",
        "checkIp: IP 주소 검증 여부 (기본값: false)",
        "allowedIps: 허용된 IP 주소 목록",
        "rateLimit: 요청 제한 횟수 (기본값: 100)",
        "rateLimitPeriod: 요청 제한 기간(초) (기본값: 60)"
    }
)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Security {
    String[] roles() default {};
    String[] permissions() default {};
    boolean requireAuth() default true;
    boolean checkIp() default false;
    String[] allowedIps() default {};
    int rateLimit() default 100;
    int rateLimitPeriod() default 60;
} 