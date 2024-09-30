package com.tree.tree.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static final Long EXPIRATION_MILLIS = 60 * 60 * 1000L;
    private static final String ROLES = "roles";

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private Key key;

    private final ReactiveUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(final String userPk, final List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put(ROLES, roles.stream()
                .map(role -> "ROLE_" + role)
                .toList());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION_MILLIS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Mono<Authentication> getAuthentication(final String token) {
        final String username = extractUsername(token);
        return userDetailsService.findByUsername(username)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                ));
    }

    public String resolveToken(final ServerWebExchange exchange) {
        final String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("잘못된 JWT 토큰입니다: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("서명이 유효하지 않습니다: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 비어 있습니다: " + e.getMessage());
        }
        return false;
    }

    private Claims getClaim(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private String extractUsername(final String token) {
        return getClaim(token).getSubject();
    }
}
