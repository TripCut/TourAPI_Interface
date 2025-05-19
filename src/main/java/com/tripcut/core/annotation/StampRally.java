package com.tripcut.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "스탬프 랠리 기능을 위한 어노테이션",
    examples = {
        "@StampRally(dramaId = \"drama1\", requiredLocations = {\"location1\", \"location2\"})",
        "@StampRally(dramaId = \"drama2\", validateLocation = false, maxDistance = 200)"
    },
    notes = {
        "드라마 촬영지 방문 인증 및 스탬프 수집을 관리합니다.",
        "dramaId: 스탬프 랠리가 진행되는 드라마 ID",
        "requiredLocations: 필수 방문 장소 목록",
        "validateLocation: 위치 검증 여부 (기본값: true)",
        "maxDistance: 위치 검증 최대 거리(미터) (기본값: 100)",
        "위치 검증은 GPS 좌표를 기준으로 합니다."
    }
)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StampRally {
    String dramaId(); // 드라마 ID
    String[] requiredLocations(); // 필수 방문 장소
    boolean validateLocation() default true; // 위치 검증 여부
    int maxDistance() default 100; // 위치 검증 최대 거리(미터)
} 