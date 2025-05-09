package com.tripcut.core.annotation;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented

/*
@Description : 사용자 요청 개인화 정보 적용용 어노테이션
사용자의 세션 정보 또는 토큰 기반으로 사용자 설정을 주입한다
 */

public @interface PersonalizedUser {
}
