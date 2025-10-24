package com.tripcut.global.security.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ 허용할 Origin
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://tripcut.co.kr"
        ));

        // ✅ 허용할 HTTP 메서드
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // ✅ 허용할 헤더
        configuration.setAllowedHeaders(List.of("*"));

        // ✅ 자격 증명(쿠키, Authorization 등) 허용
        configuration.setAllowCredentials(true);

        // ✅ 캐시 시간 (초 단위)
        configuration.setMaxAge(3600L);

        // ✅ 모든 경로에 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
