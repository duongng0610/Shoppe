package com.e_cormerce.shoppe.controller.home.category;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.category.CategoryAnalysisService;
import com.e_cormerce.shoppe.service.category.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
  CategoryService categoryService;
  CategoryAnalysisService categoryAnalysisService;

  @GetMapping("{id}/children")
  public ResponseEntity<ApiResponse> getChildren(@PathVariable String id) {
    var result = categoryService.getChildren(id);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .data(result)
            .success(true)
            .message("get children categories successfully")
            .build());
  }

  @GetMapping("/default")
  public ResponseEntity<ApiResponse> getDefault() {
    var result = categoryService.getDefault();
    return ResponseEntity.ok(
        ApiResponse.builder()
            .data(result)
            .success(true)
            .message("get default categories successfully")
            .build());
  }

  @GetMapping("/{id}/details")
  public ResponseEntity<ApiResponse> getDetails(@PathVariable String id) {
    var res = categoryService.getCategoryDetailResponse(id);
    return ResponseEntity.ok(ApiResponse.builder().success(true).data(res).build());
  }

  @GetMapping("{id}/products")
  public ResponseEntity<ApiResponse> searchProductsInCategories(
      @PathVariable String id,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "0") int offset) {
    var result = categoryService.getProductsByCategoryId(id, limit, offset);
    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .data(result)
                .message("get suggest category successfully")
                .build());
  }

  @GetMapping("/top-searched")
  public ResponseEntity<ApiResponse> getTopSearchedCategories(
      @RequestParam(defaultValue = "3") int days, @RequestParam(defaultValue = "3") int limit) {

    var result = categoryAnalysisService.getTopSearchedCategories(days, limit);
    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .data(result)
                .message("Get top searched categories successfully")
                .build());
  }
}
