// src/main/java/com/example/demo/WebConfig.java
package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/greeting")
            // ADDED "null" TO THE ALLOWED ORIGINS LIST
            .allowedOrigins("http://localhost:9000", "http://127.0.0.1:9000", "http://127.0.0.1:5500", "null")
            .allowedMethods("GET")
            .allowedHeaders("*");
    }
}