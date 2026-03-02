package com.matrimony.auth.security;

import com.matrimony.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final TokenProperties tokenProperties;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        log.info("Generating token for user {}", user.getUserId());
        return Jwts.builder().subject(user.getUserId()).claim("role", user.getRole().name())   // 👈 ADD ROLE
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + tokenProperties.getExpiry())).signWith(getSigningKey(), Jwts.SIG.HS256).compact();
    }

    public String extractUsername(String token) {
        log.info("Get username from");
        Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }


}
