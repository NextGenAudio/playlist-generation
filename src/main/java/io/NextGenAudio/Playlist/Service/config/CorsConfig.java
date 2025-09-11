package io.NextGenAudio.Playlist.Service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000", "http://localhost:3001")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                .allowedHeaders(
                        "Accept",
                        "Accept-Language",
                        "Content-Language",
                        "Content-Type",
                        "Authorization",
                        "X-Requested-With",
                        "apikey",           // Your custom header
                        "Prefer",           // The problematic header
                        "prefer",           // Also allow lowercase version
                        "Cache-Control",
                        "Pragma"
                )
                .exposedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}