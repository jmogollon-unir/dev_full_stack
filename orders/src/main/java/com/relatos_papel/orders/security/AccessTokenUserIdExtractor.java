package com.relatos_papel.orders.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Misma clave HMAC que {@code com.relato_papel.users.utils.JwtUtils} para validar el JWT
 * que el API Gateway inyecta en el header {@code accessToken}.
 */
@Component
public class AccessTokenUserIdExtractor {

    private static final String SECRET_KEY = "relatos_papel-users-secret-key-for-jwt-token-generation-and-validation-2026";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public Integer extractUserId(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return null;
        }
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
            if (claims.getExpiration() != null && claims.getExpiration().before(new Date())) {
                return null;
            }
            return claims.get("userId", Integer.class);
        } catch (Exception e) {
            return null;
        }
    }
}
