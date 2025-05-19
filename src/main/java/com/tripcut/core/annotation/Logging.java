package com.tripcut.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "메서드 실행 로깅을 위한 어노테이션",
    examples = {
        "@Logging(level = \"INFO\", includeArgs = true)",
        "@Logging(level = \"DEBUG\", includeResult = true)"
    },
    notes = {
        "메서드의 실행 정보를 로깅합니다.",
        "level: 로깅 레벨 (INFO, DEBUG, WARN, ERROR) (기본값: INFO)",
        "includeArgs: 메서드 인자 포함 여부 (기본값: true)",
        "includeResult: 메서드 결과 포함 여부 (기본값: false)",
        "includeExecutionTime: 실행 시간 포함 여부 (기본값: true)",
        "로그는 SLF4J를 통해 출력됩니다."
    }
)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
    String level() default "INFO";
    boolean includeArgs() default true;
    boolean includeResult() default false;
    boolean includeExecutionTime() default true;
} 