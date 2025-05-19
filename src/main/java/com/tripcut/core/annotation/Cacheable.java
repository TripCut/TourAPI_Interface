package com.tripcut.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "메서드 결과 캐싱을 위한 어노테이션",
    examples = {
        "@Cacheable(key = \"user:{#userId}\", ttl = 3600)",
        "@Cacheable(key = \"drama:{#dramaId}:reviews\", ttl = 1800, refresh = true)"
    },
    notes = {
        "메서드의 실행 결과를 캐시에 저장하고 재사용합니다.",
        "key: 캐시 키 (SpEL 표현식 사용 가능)",
        "ttl: 캐시 유효 시간(초) (기본값: 3600)",
        "refresh: 캐시 갱신 여부 (기본값: false)",
        "condition: 캐싱 조건 (SpEL 표현식)",
        "unless: 캐싱 제외 조건 (SpEL 표현식)",
        "Redis를 캐시 저장소로 사용합니다."
    }
)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {
    String key() default "";
    int ttl() default 3600;
    boolean refresh() default false;
    String condition() default "";
    String unless() default "";
} 