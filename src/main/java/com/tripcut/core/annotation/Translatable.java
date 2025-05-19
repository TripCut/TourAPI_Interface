package com.tripcut.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "다국어 번역 처리용 어노테이션",
    examples = {
        "@Translatable(targetLang = \"en\")",
        "@Translatable(targetLang = \"ja\")"
    },
    notes = {
        "글로벌 사용자 대상 다국어 번역을 위해 메서드 단이나 DTO 필드 단에서 처리합니다.",
        "targetLang: 대상 언어 코드 (기본값: en)",
        "번역 AOP 또는 직렬화 시점에서 메시지 변환 모듈 연동 (MessageSource, GPT 등)",
        "자동으로 사용자의 선호 언어에 맞춰 콘텐츠를 변환합니다.",
        "캐시를 통해 번역 성능을 최적화합니다."
    }
)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Translatable {
    String targetLang() default "en";
}
