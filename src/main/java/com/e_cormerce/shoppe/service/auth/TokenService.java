package com.e_cormerce.shoppe.service.auth;

import com.e_cormerce.shoppe.entity.token.RefreshToken;
import com.e_cormerce.shoppe.entity.user.Role;
import com.e_cormerce.shoppe.repository.token.RefreshTokenRepository;
import com.e_cormerce.shoppe.util.HashUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TokenService {
    JwtService jwtService;
    RefreshTokenRepository refreshTokenRepository;

    public String generateAccessToken(String userId, Role role) {
        RefreshToken refreshToken =
                RefreshToken.builder()
                        .val(HashUtil.sha256(jwtService.generateRefreshToken(userId)))
                        .date(LocalDate.now())
                        .build();

        refreshTokenRepository.save(refreshToken);

        return jwtService.generateAccessToken(userId, role, refreshToken.getId());
    }
}
