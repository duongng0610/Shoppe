package com.e_cormerce.shoppe.controller;

import com.e_cormerce.shoppe.dto.request.LogInRequest;
import com.e_cormerce.shoppe.dto.request.LogOutRequest;
import com.e_cormerce.shoppe.dto.request.RegisterRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.LogInResponse;
import com.e_cormerce.shoppe.dto.response.RegisterResponse;
import com.e_cormerce.shoppe.dto.response.VerifyResponse;
import com.e_cormerce.shoppe.properties.CookieTokenProperties;
import com.e_cormerce.shoppe.service.AuthService;
import com.e_cormerce.shoppe.util.TokenCookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AuthController {
    AuthService authService;
    CookieTokenProperties cookieTokenProperties;

    @GetMapping("/login")
    public ResponseEntity<ApiResponse<LogInResponse>> logIn(@RequestBody  LogInRequest request, HttpServletResponse response) {
        var result = authService.logIn(request);

        Cookie cookie = new Cookie("access_token", result.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieTokenProperties.getExpirationTime());
        response.addCookie(cookie);


        return ResponseEntity.ok(ApiResponse.<LogInResponse>builder()
                        .data(result)
                        .success(true)
                        .message("login successfully")
                        .build());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        var result = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RegisterResponse>builder()
                        .data(result)
                        .message("register successfully")
                        .success(true)
                        .build());
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logOut(HttpServletRequest request,HttpServletResponse response) {

        authService.logOut(LogOutRequest.builder()
                            .accessToken(TokenCookieUtil.getAccessToken(request))
                            .build());

        Cookie cookie = new Cookie("accessToken", "0");

        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                        .success(true)
                        .message("logout successfully")
                        .build());
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyResponse>> verify() {
        var result = authService.verify();

        return ResponseEntity.ok().body(ApiResponse.<VerifyResponse>builder()
                        .message("verify user successfully")
                        .success(true)
                        .data(result)
                        .build());
    }
}
