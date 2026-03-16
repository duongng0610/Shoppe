package com.e_cormerce.shoppe.controller.admin;

import com.e_cormerce.shoppe.dto.request.admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.CreateCategoryResponse;
import com.e_cormerce.shoppe.service.category.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
  CategoryService categoryService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(path = "/categories", consumes = "multipart/form-data")
  public ResponseEntity<ApiResponse<CreateCategoryResponse>> create(
      @RequestPart CreateCategoryRequest request, @RequestPart MultipartFile thumbnail) {
    var result = categoryService.create(request, thumbnail);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.<CreateCategoryResponse>builder()
                .data(result)
                .success(true)
                .message("create category successfully")
                .build());
  }
}
