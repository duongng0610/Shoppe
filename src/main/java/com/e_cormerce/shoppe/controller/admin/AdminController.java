package com.e_cormerce.shoppe.controller.admin;

import com.e_cormerce.shoppe.dto.request.admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.admin.AdminService;
import com.e_cormerce.shoppe.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
  CategoryService categoryService;
  AdminService adminService;

  @PreAuthorize("hasAuthority('PERMISSION_ADD_CATEGORY')")
  @PostMapping(path = "/categories")
  public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
    categoryService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.builder().success(true).message("create category successfully").build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_APPROVE_PRODUCTS')")
  @PatchMapping(path = "/products/{id}/approve")
  public ResponseEntity<ApiResponse> approveProducts(@PathVariable String id) {
    adminService.approveProducts(id);
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.builder().success(true).message("approve products successfully").build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_REJECT_PRODUCTS')")
  @PatchMapping(path = "/products/{id}/reject")
  public ResponseEntity<ApiResponse> rejectProducts(@PathVariable String id) {
    adminService.rejectProducts(id);
    return ResponseEntity.ok(
        ApiResponse.builder().success(true).message("reject products successfully").build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_BAN_PRODUCTS')")
  @PatchMapping(path = "/products/{id}/ban")
  public ResponseEntity<ApiResponse> banProducts(@PathVariable String id) {
    adminService.banProducts(id);
    return ResponseEntity.ok(
        ApiResponse.builder().success(true).message("ban products successfully").build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_UNLOCK_PRODUCTS')")
  @PatchMapping(path = "/products/{id}/unlock")
  public ResponseEntity<ApiResponse> unlockProducts(@PathVariable String id) {
    adminService.unlockProducts(id);
    return ResponseEntity.ok(
        ApiResponse.builder().success(true).message("unlock products successfully").build());
  }

  // Client Management Endpoints
  @PreAuthorize("hasAuthority('PERMISSION_GET_USERS_MANAGEMENT')")
  @GetMapping(path = "/clients")
  public ResponseEntity<ApiResponse> getClientInfo(
      @RequestParam int limit, @RequestParam int offset) {
    var clients = adminService.getClientInfo(limit, offset);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .success(true)
            .message("get client info successfully")
            .data(clients)
            .build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_GET_USER_DETAILS_MANAGEMENT')")
  @GetMapping(path = "/clients/{clientId}/detail")
  public ResponseEntity<ApiResponse> getClientDetailInfo(@PathVariable String clientId) {
    var client = adminService.getClientDetailInfo(clientId);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .success(true)
            .message("get client detail info successfully")
            .data(client)
            .build());
  }

  // Seller Management Endpoints
  @PreAuthorize("hasAuthority('PERMISSION_GET_USERS_MANAGEMENT')")
  @GetMapping(path = "/sellers/registered")
  public ResponseEntity<ApiResponse> getRegisteredSellerInfo(
      @RequestParam int limit, @RequestParam int offset) {
    var sellers = adminService.getRegisteredSellerInfo(limit, offset);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .success(true)
            .message("get registered seller info successfully")
            .data(sellers)
            .build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_GET_USERS_MANAGEMENT')")
  @GetMapping(path = "/sellers/unregistered")
  public ResponseEntity<ApiResponse> getUnregisteredSellerInfo(
      @RequestParam int limit, @RequestParam int offset) {
    var sellers = adminService.getUnregisteredSellerInfo(limit, offset);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .success(true)
            .message("get unregistered seller info successfully")
            .data(sellers)
            .build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_GET_USER_DETAILS_MANAGEMENT')")
  @GetMapping(path = "/sellers/{sellerId}/detail")
  public ResponseEntity<ApiResponse> getSellerDetailInfo(@PathVariable String sellerId) {
    var seller = adminService.getSellerDetailInfo(sellerId);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .success(true)
            .message("get seller detail info successfully")
            .data(seller)
            .build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_APPROVE_SELLER_REGISTER')")
  @PatchMapping(path = "/sellers/{sellerId}/approve")
  public ResponseEntity<ApiResponse> approveSellerRegister(@PathVariable String sellerId) {
    adminService.approveSellerRegister(sellerId);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .success(true)
            .message("approve seller register successfully")
            .build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_REJECT_SELLER_REGISTER')")
  @PatchMapping(path = "/sellers/{sellerId}/reject")
  public ResponseEntity<ApiResponse> rejectSellerRegister(@PathVariable String sellerId) {
    adminService.rejectSellerRegister(sellerId);
    return ResponseEntity.ok(
        ApiResponse.builder().success(true).message("reject seller register successfully").build());
  }

  // User Ban/Unban Endpoints
  @PreAuthorize("hasAuthority('PERMISSION_LOCK_USER')")
  @PatchMapping(path = "/users/{userId}/ban")
  public ResponseEntity<ApiResponse> banUser(@PathVariable String userId) {
    adminService.banUser(userId);
    return ResponseEntity.ok(
        ApiResponse.builder().success(true).message("ban user successfully").build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_UNLOCK_USER')")
  @PatchMapping(path = "/users/{userId}/unban")
  public ResponseEntity<ApiResponse> unbanUser(@PathVariable String userId) {
    adminService.unbanUser(userId);
    return ResponseEntity.ok(
        ApiResponse.builder().success(true).message("unban user successfully").build());
  }
}
