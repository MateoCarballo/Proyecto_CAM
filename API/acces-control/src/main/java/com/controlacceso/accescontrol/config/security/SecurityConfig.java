package com.controlacceso.accescontrol.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(ApiKeyFilter apiKeyFilter,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.apiKeyFilter = apiKeyFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para APIs REST
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (sin autenticación)
                        .requestMatchers("/auth/login", "/auth/register").permitAll()

                        // Endpoint protegido por API Key
                        .requestMatchers("/card/read").authenticated()

                        // Endpoints protegidos por JWT
                        .requestMatchers("/horarios/**").authenticated()

                        // Todo lo demás se permite
                        .anyRequest().permitAll()
                )

                // 🔹 El filtro JWT debe ir antes del UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 🔹 El filtro de API key se coloca después del JWT (para no interferir)
                .addFilterAfter(apiKeyFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}
