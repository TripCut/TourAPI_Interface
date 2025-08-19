package com.tripcut.global.common.api;

import lombok.Getter;

@Getter
public class ApiPath {
    public static final String BASE_URL = "/api/v1";

    // 기본 버전
    public static final String API_VERSION = "v1";

    // 고도화 사용 버전
    public static final String API_VERSION2 = "v2";

    // 테스트시 사용 버전
    public static final String API_TEST= "v_test";
}
