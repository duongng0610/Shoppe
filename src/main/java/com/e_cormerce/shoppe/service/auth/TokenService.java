package com.e_cormerce.shoppe.service.auth;

import com.e_cormerce.shoppe.entity.token.RefreshToken;
import com.e_cormerce.shoppe.entity.user.Role;
import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.repository.token.RefreshTokenRepository;
import com.e_cormerce.shoppe.util.HashUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TokenService {
    JwtService jwtService;
    RefreshTokenRepository refreshTokenRepository;
    JwtProperties jwtProperties;

    public String generateAccessToken(String userId, Role role) {
        RefreshToken refreshToken =
                RefreshToken.builder()
                        .val(HashUtil.sha256(jwtService.generateRefreshToken(userId)))
                        .expiredAt(LocalDateTime.now().plus(jwtProperties.getRefreshTokenExpirationTime(), ChronoUnit.MILLIS))
                        .build();

        refreshTokenRepository.save(refreshToken);

        return jwtService.generateAccessToken(userId, role, refreshToken.getId());
    }
}
