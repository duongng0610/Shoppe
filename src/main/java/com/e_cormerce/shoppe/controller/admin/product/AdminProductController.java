package com.e_cormerce.shoppe.controller.admin.product;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.admin.AdminService;
import com.e_cormerce.shoppe.service.product.ProductAnalysisService;
import com.e_cormerce.shoppe.service.product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminProductController {
    ProductAnalysisService productAnalysisService;
    AdminService adminService;
    ProductService productService;

    // @PreAuthorize("hasAuthority('PERMISSION_MANAGE_PRODUCT')")
    @GetMapping("/{productId}/daily-recent")
    public ResponseEntity<ApiResponse> getProductDailyRecent(
            @PathVariable String productId,
            @RequestParam(required = false, defaultValue = "7") Integer days) {

        var result = productAnalysisService.getProductDailyRecent(productId, days);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Get product daily recent successfully")
                        .data(result)
                        .build());
    }

    @PreAuthorize("hasAuthority('PERMISSION_APPROVE_PRODUCTS')")
    @PutMapping(path = "/{id}/approve")
    public ResponseEntity<ApiResponse> approveProducts(@PathVariable String id) {
        adminService.approveProducts(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.builder().success(true).message("approve products successfully").build());
    }

    @PreAuthorize("hasAuthority('PERMISSION_REJECT_PRODUCTS')")
    @PutMapping(path = "/{id}/reject")
    public ResponseEntity<ApiResponse> rejectProducts(@PathVariable String id) {
        adminService.rejectProducts(id);
        return ResponseEntity.ok(
                ApiResponse.builder().success(true).message("reject products successfully").build());
    }

    @PreAuthorize("hasAuthority('PERMISSION_BAN_PRODUCTS')")
    @PutMapping(path = "/{id}/ban")
    public ResponseEntity<ApiResponse> banProducts(@PathVariable String id) {
        adminService.banProducts(id);
        return ResponseEntity.ok(
                ApiResponse.builder().success(true).message("ban products successfully").build());
    }

    @PreAuthorize("hasAuthority('PERMISSION_UNLOCK_PRODUCTS')")
    @PutMapping(path = "/{id}/unlock")
    public ResponseEntity<ApiResponse> unlockProducts(@PathVariable String id) {
        adminService.unlockProducts(id);
        return ResponseEntity.ok(
                ApiResponse.builder().success(true).message("unlock products successfully").build());
    }

    // =====================================
    // PRODUCT FULL VIEW
    // =====================================

    @GetMapping("")
    public ResponseEntity<ApiResponse> getProductFullViews(
            @RequestParam Integer limit, @RequestParam Integer offset) {
        var result = productService.getProductFullViews(limit, offset);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Get product full views successfully")
                        .data(result)
                        .build());
    }

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse> getProductOverview(
    ) {
        var result = productService.getProductSystems();

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Get product full views successfully")
                        .data(result)
                        .build());
    }

    @GetMapping("/statistic")
    public ResponseEntity<ApiResponse> getStatisticProduct(
            @RequestParam Integer limit, @RequestParam Integer offset) {
        var result = productAnalysisService.getAllProductsForAdmin(limit, offset);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Get statistic product successfully")
                        .data(result)
                        .build());
    }
}
