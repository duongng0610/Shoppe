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
            @Valid @RequestBody CreateCategoryRequest request) {
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
                ApiResponse.builder()
                        .success(true)
                        .message("reject products successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('PERMISSION_BAN_PRODUCTS')")
    @PatchMapping(path = "/products/{id}/ban")
    public ResponseEntity<ApiResponse> banProducts(@PathVariable String id) {
        adminService.banProducts(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("ban products successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('PERMISSION_UNLOCK_PRODUCTS')")
    @PatchMapping(path = "/products/{id}/unlock")
    public ResponseEntity<ApiResponse> unlockProducts(@PathVariable String id) {
        adminService.unlockProducts(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("unlock products successfully")
                        .build()
        );
    }
}
