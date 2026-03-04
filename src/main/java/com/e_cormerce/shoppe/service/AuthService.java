package com.e_cormerce.shoppe.service;

import com.e_cormerce.shoppe.dto.request.LogInRequest;
import com.e_cormerce.shoppe.dto.request.LogOutRequest;
import com.e_cormerce.shoppe.dto.request.RegisterRequest;
import com.e_cormerce.shoppe.dto.response.LogInResponse;
import com.e_cormerce.shoppe.dto.response.LogOutResponse;
import com.e_cormerce.shoppe.dto.response.RegisterResponse;
import com.e_cormerce.shoppe.entity.token.InvalidToken;
import com.e_cormerce.shoppe.entity.token.RefreshToken;
import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.repository.*;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class AuthService {
    UserRepository userRepository;
    AccountRepository accountRepository;
    RoleRepository roleRepository;
    RefreshTokenRepository refreshTokenRepository;
    InvalidTokenRepository invalidTokenRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;
    JwtService jwtService;
    TokenService tokenService;

    JwtProperties jwtProperties;


    public LogInResponse logIn(LogInRequest request) {
        var account = accountRepository.findByEmail(request.getEmail());

        if(account == null) {
            throw new AppException(ErrorCode.INVALID_ACCOUNT);
        }

        boolean authenticated = bCryptPasswordEncoder.matches(request.getPassword(), account.getPassword());

        if(!authenticated) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        var user = account.getUser();
        var accessToken = tokenService.generateAccessToken(user);

        return LogInResponse.builder()
                .email(account.getEmail())
                .hashedPassword(bCryptPasswordEncoder.encode(request.getPassword()))
                .accessToken(accessToken)
                .build();

    }


    public RegisterResponse register(RegisterRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EXISTED_ACCOUNT);
        }

        var account = Account.builder()
                .email(request.getEmail())
                .created_at(new Date())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build();

        var role = roleRepository.findByVal(request.getRole());

        if (role == null) {
            throw new AppException(ErrorCode.INVALID_ROLE);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.INVALID_USERNAME);
        }

        var user = User.builder()
                .account(account)
                .username(request.getUsername())
                .role(role)
                .created_at(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return RegisterResponse.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .role(user.getRole())
                .hashedPassword(bCryptPasswordEncoder.encode(request.getPassword()))
                .build();
    }

    public LogOutResponse logOut(LogOutRequest request) {
        Claims accessTokenClaims = jwtService.extractClaims(request.getAccessToken(), jwtProperties.getAccessTokenSecret());

        String refreshTokenId = (String) accessTokenClaims.get("refreshTokenId");

        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

        InvalidToken invalidToken = InvalidToken.builder()
                .val(refreshToken.getVal())
                .invalid_date(LocalDate.now())
                .build();

        invalidTokenRepository.save(invalidToken);

        return LogOutResponse.builder()
                .message("logout successfully")
                .build();
    }

}
