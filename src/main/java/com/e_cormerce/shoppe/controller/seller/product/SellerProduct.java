package com.e_cormerce.shoppe.controller.seller.product;


import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.product.MyProductResponse;
import com.e_cormerce.shoppe.service.seller.SellerService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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
@RequestMapping("/seller/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerProduct {
    SellerService sellerService;


    @GetMapping("")
    //    @PreAuthorize("hasAuthority('PERMISSION_VIEW_MY_PRODUCTS')")
    public ResponseEntity<ApiResponse<List<MyProductResponse>>> getAllProducts(
            @PathParam("limit") int limit, @PathParam("offset") int offset) {
        var res = sellerService.getMyProducts(limit, offset);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<MyProductResponse>>builder()
                                .data(res)
                                .success(true)
                                .message("get my product successfully")
                                .build());
    }

    /**
     * POST: /seller/products. -Tạo sản phẩm với request gửi lên phải có content-type:multipart/
     * form-data.
     *
     * @return
     */
    @PostMapping()
    @PreAuthorize("hasAuthority('PERMISSION_CREATE_PRODUCT')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateProductRequest request) {

        sellerService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder().success(true).message("create product successfully").build());
    }

    @PreAuthorize("hasAuthority('PERMISSION_HIDE_PRODUCTS')")
    @PatchMapping("/products/{id}/hidden")
    public ResponseEntity<ApiResponse> hiddenProduct(@PathVariable String id) {
        sellerService.hiddenProduct(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("hidden product successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('PERMISSION_HIDE_PRODUCTS')")
    @PatchMapping("/products/{id}/unhidden")
    public ResponseEntity<ApiResponse> unhiddenProduct(@PathVariable String id) {
        sellerService.unhiddenProduct(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("unhidden product successfully")
                        .build()
        );
    }


}
