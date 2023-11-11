package com.coopera.cooperaApp.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.coopera.cooperaApp.security.SecurityUtils.JWT_SECRET;

@Configuration
public class GeneralConfig {

    @Value(JWT_SECRET)
    private String secret;

    @Bean
    public JwtUtil jwtUtils() {
        return new JwtUtil(secret);
    }


}
