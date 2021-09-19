package com.github.mimsic.base.web.security.config;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

public class SecurityBeanFactory {

    public SecurityBeanFactory() {
    }

    public static CorsConfigurationSource corsConfigurationSource(SecurityConfig securityConfig) {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        securityConfig.getCors().forEach((corsFilterUnit) -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(corsFilterUnit.isAllowCredentials());
            config.setAllowedOrigins(corsFilterUnit.getAllowedOrigins());
            config.setAllowedHeaders(corsFilterUnit.getAllowedHeaders());
            config.setAllowedMethods(corsFilterUnit.getAllowedMethods());
            source.registerCorsConfiguration(corsFilterUnit.getAllowedPath(), config);
        });
        return source;
    }
}
