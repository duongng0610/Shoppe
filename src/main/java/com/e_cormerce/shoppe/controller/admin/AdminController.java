package com.e_cormerce.shoppe.controller.admin;

import com.e_cormerce.shoppe.dto.request.admin.ApproveProductsRequest;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
  CategoryService categoryService;
  AdminService adminService;

  @PreAuthorize("hasAuthority('PERMISSION_ADD_CATEGORY')")
  @PostMapping(path = "/categories", consumes = "multipart/form-data")
  public ResponseEntity<ApiResponse> create(
      @Valid @RequestPart CreateCategoryRequest request, @RequestPart MultipartFile thumbnail) {
    categoryService.create(request, thumbnail);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.builder().success(true).message("create category successfully").build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_APPROVE_PRODUCTS')")
  @PatchMapping(path = "/products/approve", consumes = "application/json")
  public ResponseEntity<ApiResponse> approveProducts(@RequestBody ApproveProductsRequest request) {
    adminService.approveProducts(request.getProductIds());
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.builder().success(true).message("approve products successfully").build());
  }
}
