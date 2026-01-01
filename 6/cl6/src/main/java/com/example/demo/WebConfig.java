package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/status")
            .allowedOrigins("http://localhost:9001", "http://127.0.0.1:9001", "null")
            .allowedMethods("GET")
            .allowedHeaders("*");
    }
}