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

  @PreAuthorize("hasAuthority('PERMISSION_APPROVE_PRODUCTS')")
  @PatchMapping(path = "/{id}/approve")
  public ResponseEntity<ApiResponse> approveProducts(@PathVariable String id) {
    adminService.approveProducts(id);
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.builder().success(true).message("approve products successfully").build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_REJECT_PRODUCTS')")
  @PatchMapping(path = "/{id}/reject")
  public ResponseEntity<ApiResponse> rejectProducts(@PathVariable String id) {
    adminService.rejectProducts(id);
    return ResponseEntity.ok(
        ApiResponse.builder().success(true).message("reject products successfully").build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_BAN_PRODUCTS')")
  @PatchMapping(path = "/{id}/ban")
  public ResponseEntity<ApiResponse> banProducts(@PathVariable String id) {
    adminService.banProducts(id);
    return ResponseEntity.ok(
        ApiResponse.builder().success(true).message("ban products successfully").build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_UNLOCK_PRODUCTS')")
  @PatchMapping(path = "/{id}/unlock")
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
}
