package com.tripcut.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Description(
    value = "투어 코스 추천을 위한 어노테이션",
    examples = {
        "@TourRecommendation(maxDramaSelections = 3, considerTraffic = true)",
        "@TourRecommendation(includeNearbyAttractions = false, maxLocations = 5)"
    },
    notes = {
        "사용자 맞춤형 투어 코스를 추천하는 기능을 제공합니다.",
        "maxDramaSelections: 최대 드라마 선택 개수 (기본값: 3)",
        "includeNearbyAttractions: 주변 관광지 포함 여부 (기본값: true)",
        "considerTraffic: 교통 상황 고려 여부 (기본값: true)",
        "considerWeather: 날씨 고려 여부 (기본값: true)",
        "maxLocations: 최대 추천 장소 수 (기본값: 10)",
        "실시간 교통 및 날씨 정보는 외부 API를 통해 수집됩니다."
    }
)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TourRecommendation {
    int maxDramaSelections() default 3; // 최대 드라마 선택 개수
    boolean includeNearbyAttractions() default true; // 주변 관광지 포함 여부
    boolean considerTraffic() default true; // 교통 상황 고려 여부
    boolean considerWeather() default true; // 날씨 고려 여부
    int maxLocations() default 10; // 최대 추천 장소 수
} 