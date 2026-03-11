package com.e_cormerce.shoppe.controller.auth;

import com.e_cormerce.shoppe.dto.request.auth.LogInRequest;
import com.e_cormerce.shoppe.dto.request.auth.LogOutRequest;
import com.e_cormerce.shoppe.dto.request.auth.RegisterRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.LogInResponse;
import com.e_cormerce.shoppe.dto.response.RegisterResponse;
import com.e_cormerce.shoppe.dto.response.VerifyResponse;
import com.e_cormerce.shoppe.properties.CookieTokenProperties;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

  AuthService authService;
  CookieTokenProperties cookieTokenProperties;
  CookieUtil cookieUtil;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LogInResponse>> logIn(
      @Valid @RequestBody LogInRequest request, HttpServletResponse response) {
    var result = authService.logIn(request);
    ResponseCookie cookie =
        cookieUtil.generateCookie(
            "access_token", result.getAccessToken(), cookieTokenProperties.getExpirationTime());

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    return ResponseEntity.ok(
        ApiResponse.<LogInResponse>builder()
            .data(result)
            .success(true)
            .message("login successfully")
            .build());
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<RegisterResponse>> register(
      @Valid @RequestBody RegisterRequest request) {
    var result = authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.<RegisterResponse>builder()
                .data(result)
                .message("register successfully")
                .success(true)
                .build());
  }

  @GetMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logOut(
      HttpServletRequest request, HttpServletResponse response) {

    authService.logOut(
        LogOutRequest.builder().accessToken(cookieUtil.getAccessToken(request)).build());

    ResponseCookie cookie = cookieUtil.generateCookie("access_token", "0", 0);

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(
        ApiResponse.<Void>builder().success(true).message("logout successfully").build());
  }

  @GetMapping("/verify")
  public ResponseEntity<ApiResponse<VerifyResponse>> verify() {
    var result = authService.verify();

    return ResponseEntity.ok()
        .body(
            ApiResponse.<VerifyResponse>builder()
                .message("verify user successfully")
                .success(true)
                .data(result)
                .build());
  }
}
