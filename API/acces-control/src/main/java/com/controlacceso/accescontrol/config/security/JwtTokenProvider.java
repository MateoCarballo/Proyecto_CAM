package com.controlacceso.accescontrol.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey; // clave privada para firmar los tokens

    @Value("${jwt.expiration-ms}")
    private long expirationMs; // duración del token en milisegundos

    /**
     * Genera un token JWT usando el email del usuario como "subject"
     */
    public String generarToken(String email) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(email)                   // información principal del usuario
                .setIssuedAt(ahora)                  // fecha de creación
                .setExpiration(expiracion)           // fecha de expiración
                .signWith(SignatureAlgorithm.HS512, secretKey) // firma del token
                .compact();
    }

    /**
     * Obtiene el email (subject) desde un token JWT válido.
     */
    public String obtenerEmailDeToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Verifica si un token es válido (firma + fecha de expiración).
     */
    public boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
