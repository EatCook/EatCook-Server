package com.itcook.cooking.api.global.config;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final Environment env;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION,"Authorization-refresh"));
        if (!isProdProfile()) {
            corsConfiguration.setAllowedOrigins(List.of("localhost:8080"));
        }
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }

    public Boolean isProdProfile() {
        String[] activeProfiles = env.getActiveProfiles();
        List<String> currentProfiles = Arrays.stream(activeProfiles).toList();
        return currentProfiles.contains("prod");
    }
}