package com.e_cormerce.shoppe.controller.admin;

import com.e_cormerce.shoppe.dto.request.Admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.dto.request.Admin.GetChildrenCategoryRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.CreateCategoryResponse;
import com.e_cormerce.shoppe.dto.response.GetChildrenCategoryResponse;
import com.e_cormerce.shoppe.dto.response.GetDefaultCategoryResponse;
import com.e_cormerce.shoppe.service.Category.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AdminController {
    CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "admin/categories", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<CreateCategoryResponse>> create(
            @RequestPart CreateCategoryRequest request,
            @RequestPart MultipartFile thumbnail) {
        var result = categoryService.create(request, thumbnail);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<CreateCategoryResponse>builder()
                        .data(result)
                        .success(true)
                        .message("create category successfully")
                        .build());
    }

    @GetMapping("/categories/children")
    public ResponseEntity<ApiResponse<GetChildrenCategoryResponse>> getChildren(@RequestBody GetChildrenCategoryRequest request) {
        var result = categoryService.getDirectChildren(request);
        return ResponseEntity.ok(
                ApiResponse.<GetChildrenCategoryResponse>builder()
                        .data(result)
                        .success(true)
                        .message("get children categories successfully")
                        .build());
    }

    @GetMapping("/categories/default")
    public ResponseEntity<ApiResponse<GetDefaultCategoryResponse>> getChildren() {
        var result = categoryService.getDirectChildren();
        return ResponseEntity.ok(
                ApiResponse.<GetDefaultCategoryResponse>builder()
                        .data(result)
                        .success(true)
                        .message("get default categories successfully")
                        .build());
    }
}
