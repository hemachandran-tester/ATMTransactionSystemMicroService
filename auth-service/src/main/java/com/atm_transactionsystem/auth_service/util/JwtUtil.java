package com.atm_transactionsystem.auth_service.util;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // Secret key (minimum 32 characters)
    private static final String SECRET =
            "ThisIsMySecretKeyForJwtAuthentication12345";

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    // Token validity (1 hour)
    private final long EXPIRATION = 1000 * 60 * 60;

    // Generate JWT
    public String generateToken(String username) {

        return Jwts.builder()

                .subject(username)

                .issuedAt(new Date())

                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))

                .signWith(key)

                .compact();
    }

    // Extract username
    public String extractUsername(String token) {

        return getClaims(token).getSubject();

    }

    // Validate token
    public boolean validateToken(String token, String username) {

        return username.equals(extractUsername(token))
                && !isTokenExpired(token);

    }

    // Check expiration
    private boolean isTokenExpired(String token) {

        return getClaims(token)
                .getExpiration()
                .before(new Date());

    }

    // Read Claims
    private Claims getClaims(String token) {

        return Jwts.parser()

                .verifyWith(key)

                .build()

                .parseSignedClaims(token)

                .getPayload();

    }

}