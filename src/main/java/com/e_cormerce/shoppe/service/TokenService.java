package com.e_cormerce.shoppe.service;

import com.e_cormerce.shoppe.entity.token.RefreshToken;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.repository.RefreshTokenRepository;
import com.e_cormerce.shoppe.util.HashUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class TokenService {
    JwtService jwtService;
    RefreshTokenRepository refreshTokenRepository;

    public String generateAccessToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .val(HashUtil.sha256(jwtService.generateRefreshToken(user)))
                .date(LocalDate.now())
                .build();

        refreshTokenRepository.save(refreshToken);

        return jwtService.generateAccessToken(user, refreshToken.getId());
    }
}
