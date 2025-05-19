package com.tripcut.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "입력값 검증을 위한 어노테이션",
    examples = {
        "@Validation(validateNull = true, validateEmpty = true)",
        "@Validation(validateFormat = true, format = \"yyyy-MM-dd\")"
    },
    notes = {
        "메서드 파라미터나 반환값의 유효성을 검증합니다.",
        "validateNull: null 값 검증 여부 (기본값: true)",
        "validateEmpty: 빈 값 검증 여부 (기본값: true)",
        "validateFormat: 형식 검증 여부 (기본값: false)",
        "format: 검증할 형식 (날짜, 이메일 등)",
        "customValidator: 커스텀 검증기 클래스",
        "검증 실패 시 ValidationException이 발생합니다."
    }
)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validation {
    boolean validateNull() default true;
    boolean validateEmpty() default true;
    boolean validateFormat() default false;
    String format() default "";
    Class<?> customValidator() default void.class;
} 