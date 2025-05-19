package com.tripcut.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "사용자 요청 개인화 정보 적용용 어노테이션",
    examples = {
        "@PersonalizedUser",
        "public void processUserRequest(@PersonalizedUser UserContext context)"
    },
    notes = {
        "사용자의 세션 정보 또는 토큰 기반으로 사용자 설정을 주입합니다.",
        "메서드 파라미터에 적용하여 사용자 컨텍스트를 자동으로 주입합니다.",
        "사용자의 선호도, 설정, 권한 등의 정보를 자동으로 처리합니다.",
        "세션이 없는 경우 기본값을 사용합니다.",
        "AOP를 통해 자동으로 처리됩니다."
    }
)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PersonalizedUser {
}
