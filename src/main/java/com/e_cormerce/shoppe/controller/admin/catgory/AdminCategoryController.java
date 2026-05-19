package com.e_cormerce.shoppe.controller.admin.catgory;

import com.e_cormerce.shoppe.dto.request.admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.dto.request.admin.UpdateCategoryRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.category.CategoryAnalysisService;
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
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryController {
  CategoryService categoryService;
  CategoryAnalysisService categoryAnalysisService;

  @PreAuthorize("hasAuthority('PERMISSION_ADD_CATEGORY')")
  @PostMapping(path = "")
  public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
    categoryService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.builder().success(true).message("create category successfully").build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String id) {

    var result = categoryService.deleteCategory(id);

    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .success(true)
                .message("Delete category successfully")
                .data(result)
                .build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse> updateCategory(
      @PathVariable String id, @Valid @RequestBody UpdateCategoryRequest request) {
    categoryService.update(id, request);
    return ResponseEntity.ok(
        ApiResponse.builder().success(true).message("Update category successfully").build());
  }

  //  @PreAuthorize("hasAuthority('PERMISSION_VIEW_CATEGORY')")
  @GetMapping(path = "/statistic")
  public ResponseEntity<ApiResponse> getCategories(
      @RequestParam Integer limit, @RequestParam Integer offset) {

    var result = categoryAnalysisService.getStatisticCategories(limit, offset);

    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .success(true)
                .message("Get categories successfully")
                .data(result)
                .build());
  }

  @GetMapping("/{id}/daily-statistics")
  public ResponseEntity<ApiResponse> getCategoryDailyStatistics(
      @PathVariable String id, @RequestParam(defaultValue = "7") int days) {

    var result = categoryAnalysisService.getCategoryDailyRecent(id, days);

    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .success(true)
                .message("Get category daily statistics successfully")
                .data(result)
                .build());
  }

  @GetMapping("{id}/analysis-growth")
  public ResponseEntity<ApiResponse> getCategoryGrowthAnalysis(
      @PathVariable String id, @RequestParam(defaultValue = "7") int days) {

    var result = categoryAnalysisService.getCategoryGrowthAnalysis(id, days);

    return ResponseEntity.ok(
        ApiResponse.builder()
            .success(true)
            .message("Get category growth analysis successfully")
            .data(result)
            .build());
  }
}
