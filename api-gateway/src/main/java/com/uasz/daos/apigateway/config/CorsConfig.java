package com.uasz.daos.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Origines autorisées (ajoutez vos domaines frontend ici)
        corsConfig.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",           // Tous les ports localhost
            "http://127.0.0.1:*",            // Tous les ports 127.0.0.1
            "http://localhost:3000",         // React par défaut
            "http://localhost:4200",         // Angular par défaut
            "http://localhost:8080",         // Vue.js / Gateway
            "http://localhost:5173",         // Vite par défaut
            "http://localhost:5174",         // Vite alternatif
            "https://votre-domaine.com"      // Production (à remplacer)
        ));

        // Méthodes HTTP autorisées
        corsConfig.setAllowedMethods(Arrays.asList(
            "GET",
            "POST",
            "PUT",
            "PATCH",
            "DELETE",
            "OPTIONS",
            "HEAD"
        ));

        // Headers autorisés
        corsConfig.setAllowedHeaders(Arrays.asList(
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "X-XSRF-TOKEN",
            "Cache-Control",
            "Pragma"
        ));

        // Headers exposés (visibles côté client)
        corsConfig.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Disposition",
            "X-Total-Count",
            "X-Page-Number",
            "X-Page-Size"
        ));

        // Autoriser les credentials (cookies, authorization headers, etc.)
        corsConfig.setAllowCredentials(true);

        // Durée de cache de la pré-vérification CORS (en secondes)
        corsConfig.setMaxAge(3600L); // 1 heure

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
