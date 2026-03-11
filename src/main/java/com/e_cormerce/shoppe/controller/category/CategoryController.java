package com.e_cormerce.shoppe.controller.category;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.category.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
  CategoryService categoryService;

  @GetMapping("/children")
  public ResponseEntity<ApiResponse> getChildren(@RequestParam String id) {
    var result = categoryService.getChildren(id);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .data(result)
            .success(true)
            .message("get children categories successfully")
            .build());
  }

  @GetMapping("/default")
  public ResponseEntity<ApiResponse> getChildren() {
    var result = categoryService.getDefault();
    return ResponseEntity.ok(
        ApiResponse.builder()
            .data(result)
            .success(true)
            .message("get default categories successfully")
            .build());
  }
}
