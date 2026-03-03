package com.e_cormerce.shoppe.controller;

import com.e_cormerce.shoppe.dto.request.LogInRequest;
import com.e_cormerce.shoppe.dto.request.LogOutRequest;
import com.e_cormerce.shoppe.dto.request.RegisterRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.LogInResponse;
import com.e_cormerce.shoppe.dto.response.LogOutResponse;
import com.e_cormerce.shoppe.dto.response.RegisterResponse;
import com.e_cormerce.shoppe.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AuthController {
    AuthService authService;

    @GetMapping("/log-in")
    public ResponseEntity<ApiResponse<LogInResponse>> logIn(@RequestBody  LogInRequest request) {
        var result = authService.logIn(request);
        return ResponseEntity.ok(ApiResponse.<LogInResponse>builder()
                .data(result)
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        var result = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<RegisterResponse>builder()
                .data(result)
                .build());
    }

    @GetMapping("/log-out")
    public ResponseEntity<ApiResponse<LogOutResponse>> logOut(@RequestBody LogOutRequest request) {
        var result = authService.logOut(request);
        return ResponseEntity.ok(ApiResponse.<LogOutResponse>builder()
                        .data(result)
                        .build());
    }
}
