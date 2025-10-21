package com.controlacceso.accescontrol.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "x-api-key";

    @Value("${esp32.api-key}")
    private String esp32ApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Solo proteger el endpoint /card/read
        if ("/card/read".equals(request.getRequestURI())) {
            String apiKey = request.getHeader(API_KEY_HEADER);
            if (apiKey == null || !apiKey.equals(esp32ApiKey)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("API Key inválida o no proporcionada");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
