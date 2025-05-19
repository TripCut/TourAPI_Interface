package com.tripcut.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "트랜잭션 관리를 위한 어노테이션",
    examples = {
        "@Transactional(isolation = \"READ_COMMITTED\", timeout = 30)",
        "@Transactional(rollbackFor = {Exception.class}, propagation = \"REQUIRES_NEW\")"
    },
    notes = {
        "메서드의 트랜잭션을 관리합니다.",
        "isolation: 트랜잭션 격리 수준 (기본값: DEFAULT)",
        "timeout: 트랜잭션 타임아웃(초) (기본값: 30)",
        "readOnly: 읽기 전용 트랜잭션 여부 (기본값: false)",
        "rollbackFor: 롤백할 예외 클래스 목록",
        "noRollbackFor: 롤백하지 않을 예외 클래스 목록",
        "propagation: 트랜잭션 전파 방식 (기본값: REQUIRED)",
        "Spring의 @Transactional과 유사한 기능을 제공합니다."
    }
)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
    String isolation() default "DEFAULT";
    int timeout() default 30;
    boolean readOnly() default false;
    Class<? extends Throwable>[] rollbackFor() default {};
    String[] noRollbackFor() default {};
    String propagation() default "REQUIRED";
} 