package com.yujing.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private long expire;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId) {
        return generateToken(userId, null);
    }

    public String generateToken(Long userId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expire);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        return Long.valueOf(claims.getSubject());
    }

    public String getRole(String token) {
        Claims claims = parseToken(token);
        Object role = claims.get("role");
        return role == null ? null : role.toString();
    }
}
