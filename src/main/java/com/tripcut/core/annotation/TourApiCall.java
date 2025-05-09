package com.tripcut.core.annotation;


import java.lang.annotation.*;

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