package com.tripcut.core.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented

/*
@Description : 다국어 번역 처리용 어노테이션
글로벌 사용자 대상 다국어 번역을 위해 메서드 단이나 DTO 필드 단에서 처리
번역 AOP 또는 직렬화 시점에서 메시지 변환 모듈 연동 (MessageSource, GPT 등).
 */

public @interface Translatable {
    String targetLang() default "en";
}
