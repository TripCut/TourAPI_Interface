package com.tripcut.middleware.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tripcut.core.annotation.Logging;
import com.tripcut.core.annotation.Metrics;
import com.tripcut.core.annotation.TourApiCall;

@Service
public class FastAPIService {
    
    private final RestTemplate restTemplate;
    private final String baseUrl;
    
    @Autowired
    public FastAPIService(RestTemplate restTemplate, String fastApiBaseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = fastApiBaseUrl;
    }
    
    @Logging(level = "INFO", includeArgs = true, includeResult = true)
    @Metrics(name = "fastapi.recommendation", tags = {"api=recommendation"})
    @TourApiCall
    public Object getRecommendation(String userId, String preferences) {
        String url = baseUrl + "/recommend";
        return restTemplate.postForObject(url, 
            Map.of("user_id", userId, "preferences", preferences), 
            Object.class);
    }
    
    @Logging(level = "INFO", includeArgs = true)
    @Metrics(name = "fastapi.analysis", tags = {"api=analysis"})
    @TourApiCall
    public Object analyzeUserBehavior(String userId) {
        String url = baseUrl + "/analyze";
        return restTemplate.getForObject(url + "?user_id=" + userId, Object.class);
    }
} 