package io.NextGenAudio.Playlist.Service.config;

import io.NextGenAudio.Playlist.Service.security.NextAuthSessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Autowired
    private NextAuthSessionFilter nextAuthSessionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))   // <-- use your CorsConfig bean
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()      // allow preflight
                        .requestMatchers("/public/**", "/health").permitAll()
                        .requestMatchers("/files/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(nextAuthSessionFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
