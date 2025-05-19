package com.tripcut.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "TourAPI 서비스 호출을 위한 어노테이션",
    examples = {
        "@TourAPI(service = \"location\", operation = \"search\")",
        "@TourAPI(service = \"drama\", operation = \"detail\", cache = false)"
    },
    notes = {
        "TourAPI 서비스와의 통신을 관리하고 캐싱을 지원합니다.",
        "service: API 서비스 종류 (location, drama, tour 등)",
        "operation: API 작업 유형 (search, detail, list 등)",
        "cache: 캐싱 여부 (기본값: true)",
        "cacheTTL: 캐시 유효 시간(초) (기본값: 3600)"
    }
)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TourAPI {
    String service() default ""; // TourAPI 서비스 종류
    String operation() default ""; // API 작업 유형
    boolean cache() default true; // 캐싱 여부
    int cacheTTL() default 3600; // 캐시 유효 시간(초)
} 