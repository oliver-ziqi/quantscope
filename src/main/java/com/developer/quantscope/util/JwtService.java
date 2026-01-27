package com.developer.quantscope.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ziqi
 */

@Component
public class JwtService {

    private final Key key;
    private final long expireSeconds;

    public JwtService(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expire-seconds}") long expireSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireSeconds = expireSeconds;
    }

    /**
     * Generate JWT
     */
    public String generateToken(Long userId, String username) {
        Instant now = Instant.now();
        return Jwts.builder()
            .setSubject(username)
            .claim("uid", userId)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(expireSeconds)))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Parse and verify JWT (signature verification + expiration)
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * Get user ID from JWT
     */
    public Optional<Long> getUserIdFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return Optional.ofNullable(claims.get("uid", Long.class));
        } catch (Exception e) {
            // Token parsing failed or invalid
            return Optional.empty();
        }
    }
}
