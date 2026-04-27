package com.e_cormerce.shoppe.controller.home.category;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.category.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
  CategoryService categoryService;

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
  public ResponseEntity<ApiResponse> searchProductsInCategories(@PathVariable String id) {
    var result = categoryService.getProductsByCategoryId(id);
    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .data(result)
                .message("get suggest category successfully")
                .build());
  }
}
