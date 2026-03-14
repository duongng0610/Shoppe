package com.e_cormerce.shoppe.controller.category;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.category.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class CategoryController {
    CategoryService categoryService;

    @GetMapping("/categories/{id}/children")
    public ResponseEntity<ApiResponse> getChildren(@PathVariable String id) {
        var result = categoryService.getChildren(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .data(result)
                        .success(true)
                        .message("get children categories successfully")
                        .build());
    }

    @GetMapping("/categories/default")
    public ResponseEntity<ApiResponse> getChildren() {
        var result = categoryService.getDefault();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .data(result)
                        .success(true)
                        .message("get default categories successfully")
                        .build());
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse> getCategories() {
        var result = categoryService.getDefault();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .data(result)
                        .success(true)
                        .message("get default categories successfully")
                        .build());
    }
}
