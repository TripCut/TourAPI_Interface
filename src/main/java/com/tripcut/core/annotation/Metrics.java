package com.tripcut.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "메트릭스 수집을 위한 어노테이션",
    examples = {
        "@Metrics(name = \"api.latency\", tags = {\"api=user\", \"method=get\"})",
        "@Metrics(name = \"cache.hits\", type = \"counter\")"
    },
    notes = {
        "메서드의 실행 메트릭스를 수집합니다.",
        "name: 메트릭스 이름",
        "type: 메트릭스 타입 (timer, counter, gauge) (기본값: timer)",
        "tags: 메트릭스 태그 (key=value 형식)",
        "description: 메트릭스 설명",
        "unit: 측정 단위 (ms, count 등)",
        "Prometheus 형식으로 메트릭스를 수집합니다."
    }
)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Metrics {
    String name();
    String type() default "timer";
    String[] tags() default {};
    String description() default "";
    String unit() default "";
} 