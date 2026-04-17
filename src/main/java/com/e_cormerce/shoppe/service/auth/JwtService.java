package com.e_cormerce.shoppe.service.auth;

import com.e_cormerce.shoppe.entity.user.Role;
import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.repository.user.RoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class JwtService {

    JwtProperties jwtProperties;
    RoleRepository roleRepository;

    /**
     * Tạo access token.
     */
    public String generateAccessToken(String userId, Role role, String refreshTokenId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", buildScope(role));
        claims.put("refreshTokenId", refreshTokenId);
        return createToken(
                claims,
                userId,
                jwtProperties.getAccessTokenExpirationTime(),
                jwtProperties.getAccessTokenSecret());
    }

    /**
     * Tạo refresh token.
     */
    public String generateRefreshToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(
                claims,
                userId,
                jwtProperties.getRefreshTokenExpirationTime(),
                jwtProperties.getRefreshTokenSecret());
    }

    private String createToken(
            Map<String, Object> claims, String userId, long expiration, String secretKey) {
        return Jwts.builder()
                .setHeader(
                        Map.of(
                                "typ", "JWT",
                                "alg", "HS256"))
                .addClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now().plus(expiration, ChronoUnit.SECONDS).toEpochMilli()))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .setIssuer("shoppe.com")
                .compact();
    }

    /**
     * Lấy role + permisison.
     */
    private List<String> buildScope(Role role) {
        var permissions = roleRepository.getPermissionsByRole(role.getId());
        var res = permissions.stream().map(permission -> {
            StringBuilder s = new StringBuilder("PERMISSION_");
            s.append(permission);
            return s.toString();
        }).toList();
        res.add("ROLE_" + role.getVal());

        return res;
    }

    /**
     * Chuyển secret-key -> bytes.
     */
    public Key getKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes); // dùng thuộc toán SHA nên phải đưa bề bằng hmacSha
    }

    /**
     * Giải mã token.
     */
    public String decode(String token, String secretKey) {
        Claims claims =
                Jwts.parserBuilder()
                        .setSigningKey(getKey(secretKey))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
        return claims.getSubject();
    }

    /**
     * Lấy thông tin token
     */
    public Claims extractClaims(String token, String secretKey) {

        return Jwts.parserBuilder()
                .setSigningKey(getKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Lấy authentication(principle:token.subject(userId), ,authorities.
     *
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims =
                Jwts.parserBuilder()
                        .setSigningKey(getKey(jwtProperties.getAccessTokenSecret()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
        List<GrantedAuthority> authorities = new ArrayList<>();
        claims
                .get("authorities", List.class)
                .forEach(
                        e -> {
                            authorities.add(new SimpleGrantedAuthority(e.toString()));
                        });
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
    }
}
