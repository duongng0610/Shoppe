package com.e_cormerce.shoppe.service;

import com.e_cormerce.shoppe.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {
    @Value("${jwt.secret_access_token}")
    String SECRET_ACCESS_TOKEN;

    @Value("${jwt.exp_access_token}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.secret_refresh_token}")
    String SECRET_REFRESH_TOKEN;

    @Value("${jwt.exp_refresh_token}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    /**
     * Tạo access token.
     */
    public String generateAccessToken(User user, String refreshTokenId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", buildScope(user));
        claims.put("refreshTokenId", refreshTokenId);
        return createToken(claims, user, ACCESS_TOKEN_EXPIRATION_TIME, SECRET_ACCESS_TOKEN);
    }

    /**
     * Tạo refresh token.
     */
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user, REFRESH_TOKEN_EXPIRATION_TIME, SECRET_REFRESH_TOKEN);
    }

    private String createToken(Map<String, Object> claims, User user, long expiration, String secretKey) {
        return Jwts.builder()
                .setHeader(Map.of(
                        "typ", "JWT",
                        "alg", "HS256"))
                .setClaims(claims)
                .setSubject(user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now().plus(expiration, ChronoUnit.SECONDS).toEpochMilli()))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .setId(UUID.randomUUID().toString())
                .setIssuer("shoppe.com")
                .compact();
    }

    /**
     * Lấy role + permisison.
     */
    private List<String> buildScope(User user) {
        List<String> result = new ArrayList<>();
        result.add("ROLE_" + user.getRole().getVal());
        user.getRole().getPermissions().forEach(permission -> {
            result.add("PERMISSION_" + permission.getVal());
        });
        return result;
    }

    /**
     * Chuyển secret-key -> bytes.
     */
    private Key getKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);//dùng thuộc toán SHA nên phải đưa bề bằng hmacSha
    }

    /**
     * Giải mã token.
     */
    public String decode(String token, String secretKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Lấy authentication(principle:token.subject(userId), ,authorities.
     *
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getKey(SECRET_ACCESS_TOKEN)).build().parseClaimsJws(token).getBody();
        List<GrantedAuthority> authorities = new ArrayList<>();
        claims.get("authorities", List.class).forEach(e -> {
            authorities.add(new SimpleGrantedAuthority(e.toString()));
        });
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
    }
}
