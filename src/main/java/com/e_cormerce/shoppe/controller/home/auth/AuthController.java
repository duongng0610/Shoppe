package com.e_cormerce.shoppe.controller.home.auth;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import com.e_cormerce.shoppe.dto.request.auth.login.LogInRequest;
import com.e_cormerce.shoppe.dto.request.auth.register.RegisterClientRequest;
import com.e_cormerce.shoppe.dto.request.auth.register.RegisterSellerRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.properties.CookieTokenProperties;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> logIn(
            @Valid @RequestBody LogInRequest request, HttpServletResponse response) {
        var accessToken = authService.logIn(request);

        cookieUtil.saveToken(
                "access_token", accessToken, cookieTokenProperties.getExpirationTime(), response);

        return ResponseEntity.ok(
                ApiResponse.builder().success(true).message("login successfully").build());
    }

    @PostMapping("/register/client")
    public ResponseEntity<ApiResponse> registerClient(
            @Valid @RequestBody RegisterClientRequest request) {
        authService.registerClient(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder().message("register successfully").success(true).build());
    }

    @PostMapping("/register/seller")
    public ResponseEntity<ApiResponse> registerSeller(
            @Valid @RequestBody RegisterSellerRequest request) {
        authService.registerSeller(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder().message("register successfully").success(true).build());
    }


    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logOut(
            HttpServletRequest request, HttpServletResponse response) {

        authService.logOut(cookieUtil.getAccessToken(request));

        cookieUtil.saveToken("access_token", "0", 0, response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder().success(true).message("logout successfully").build());
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<UserDto>> verify() {
        var result = authService.verify();

        return ResponseEntity.ok()
                .body(
                        ApiResponse.<UserDto>builder()
                                .message("verify user successfully")
                                .success(true)
                                .data(result)
                                .build());
    }
}
