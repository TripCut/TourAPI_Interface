package com.tripcut.middleware.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FastAPIConfig {
    
    @Value("${fastapi.base-url}")
    private String fastApiBaseUrl;
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(fastApiBaseUrl)
                .build();
    }
} 