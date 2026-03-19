package com.e_cormerce.shoppe.controller.seller;

import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.service.auth.JwtService;
import com.e_cormerce.shoppe.service.media.CloudinaryService;
import com.e_cormerce.shoppe.service.seller.SellerService;
import com.e_cormerce.shoppe.util.CookieUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerController {
    SellerService sellerService;
    CloudinaryService cloudinaryService;
    CookieUtil cookieUtil;
    JwtService jwtService;
    JwtProperties jwtProperties;

    /**
     * POST: /seller/products. -Tạo sản phẩm với request gửi lên phải có content-type:multipart/
     * form-data.
     *
     * @return
     */
    @PostMapping(path = "/product", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('SELLER') or hasPermissions('CREATE_PRODUCT')")
    public ResponseEntity<ApiResponse> create(
           @ModelAttribute CreateProductRequest request) {
        sellerService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .success(true)
                                .message("create product successfully")
                                .build());
    }
}
