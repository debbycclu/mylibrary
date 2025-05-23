package com.example.springbootdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NyplConfig {
    
    @Value("${nypl.api.key}")
    private String apiKey;
    
    @Value("${nypl.api.base-url}")
    private String baseUrl;
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
} 