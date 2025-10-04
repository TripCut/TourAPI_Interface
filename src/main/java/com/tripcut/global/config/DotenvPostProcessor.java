package com.tripcut.global.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        Map<String, Object> map = new HashMap<>();
        dotenv.entries().forEach(e -> map.put(e.getKey(), e.getValue()));

        // 가장 먼저 보이도록 앞에 추가
        environment.getPropertySources().addFirst(new MapPropertySource("dotenv", map));

        System.out.println("✅ .env loaded keys: " + map.keySet());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}