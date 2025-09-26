package com.payflow.merchant.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long accessExpMs;
    private final long refreshExpMs;
    private final String issuer;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-ttl}") Duration accessTtl,
            @Value("${jwt.refresh-ttl}") Duration refreshTtl,
            @Value("${jwt.issuer:}") String issuer
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpMs = accessTtl.toMillis();
        this.refreshExpMs = refreshTtl.toMillis();
        this.issuer = issuer;
    }

    public String generateAccessToken(Long userId, String email) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessExpMs);
        JwtBuilder b = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256);
        if (!issuer.isEmpty()) b.setIssuer(issuer);
        return b.compact();
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshExpMs);
        JwtBuilder b = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256);
        if (!issuer.isEmpty()) b.setIssuer(issuer);
        return b.compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public Long parseRefreshToken(String token) {
        Claims c = parse(token).getBody();
        return Long.valueOf(c.getSubject());
    }
}
