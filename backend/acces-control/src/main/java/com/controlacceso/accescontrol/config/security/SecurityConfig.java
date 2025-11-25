package com.controlacceso.accescontrol.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(ApiKeyFilter apiKeyFilter,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.apiKeyFilter = apiKeyFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // 🔹 Este bean permite inyectar el AuthenticationManager en tus controladores o servicios
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
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
                // Primero el filtro de API Key (porque es más específico)
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)

                // Luego el filtro JWT
                .addFilterAfter(jwtAuthenticationFilter, ApiKeyFilter.class);

        System.out.println(">=================>Configuración de seguridad personalizada cargada correctamente");

        return http.build();
    }
}
