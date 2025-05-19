package com.tripcut.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "외부 API 인증/로그 기록용 어노테이션",
    examples = {
        "@TourApiCall(\"tour-api\")",
        "@TourApiCall(\"sns-api\")"
    },
    notes = {
        "TourAPI나 SNS API 사용 시 요청 로깅이나 인증 처리를 공통화합니다.",
        "value: API 식별자 (기본값: 빈 문자열)",
        "API 호출 시 자동으로 로깅과 인증 처리가 수행됩니다.",
        "API 키 관리와 요청/응답 로깅을 자동화합니다.",
        "에러 발생 시 자동으로 재시도 로직이 적용됩니다."
    }
)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented

/*
@Description : 외부 API 인증/로그 기록용 어노테이션
TourAPI나 SNS API 사용 시 요청 로깅이나 인증 처리를 공통화한다.
 */
public @interface TourApiCall {
    String value() default "";
}