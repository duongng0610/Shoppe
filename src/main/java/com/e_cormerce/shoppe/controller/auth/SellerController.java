package com.e_cormerce.shoppe.controller.auth;

import com.e_cormerce.shoppe.dto.request.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.CreateProductResponse;
import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.service.JwtService;
import com.e_cormerce.shoppe.service.SellerService;
import com.e_cormerce.shoppe.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class SellerController {
    SellerService sellerService;
    CookieUtil cookieUtil;
    JwtService jwtService;
    JwtProperties jwtProperties;


    @PostMapping("/product")
    public ResponseEntity<ApiResponse<CreateProductResponse>> create(@RequestBody CreateProductRequest request) {
        var result = sellerService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<CreateProductResponse>builder()
                        .data(result)
                        .success(true)
                        .message("create product successfully")
                        .build());
    }
}
