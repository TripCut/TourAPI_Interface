package com.tripcut.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "다국어 지원을 위한 어노테이션",
    examples = {
        "@MultiLanguage(supportedLanguages = {\"ko\", \"en\", \"zh\"})",
        "@MultiLanguage(required = false)"
    },
    notes = {
        "필드나 메서드에 다국어 지원을 추가합니다.",
        "supportedLanguages: 지원하는 언어 코드 목록 (기본값: ko, en, zh, ja)",
        "required: 모든 지원 언어에 대한 번역이 필수인지 여부 (기본값: true)",
        "언어 코드는 ISO 639-1 표준을 따릅니다."
    }
)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiLanguage {
    String[] supportedLanguages() default {"ko", "en", "zh", "ja"}; // 지원 언어 목록
    boolean required() default true; // 필수 번역 여부
} 