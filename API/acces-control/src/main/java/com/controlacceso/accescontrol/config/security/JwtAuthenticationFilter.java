package com.controlacceso.accescontrol.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // ✅ Añadido para manejar roles
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // quitar el prefijo "Bearer "

            if (jwtTokenProvider.validarToken(token)) {

                // ✅ Extraemos el email y el rol del JWT
                String email = jwtTokenProvider.obtenerEmailDeToken(token);
                String rol = jwtTokenProvider.obtenerRolDeToken(token); // <-- Nuevo método en JwtTokenProvider

                // ✅ Creamos la lista de autoridades con el rol extraído
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase())
                );

                // ✅ Creamos el objeto de autenticación con email y roles
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                // ✅ Guardamos la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
