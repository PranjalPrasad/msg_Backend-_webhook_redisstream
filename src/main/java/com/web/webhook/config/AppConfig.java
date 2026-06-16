package com.web.webhook.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {

    @Value("${meta.verify.token}")
    private String verifyToken;

    public String getVerifyToken() { return verifyToken; }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}