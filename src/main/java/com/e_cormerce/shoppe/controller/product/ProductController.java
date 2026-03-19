package com.e_cormerce.shoppe.controller.product;

import com.e_cormerce.shoppe.dto.request.admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getProductForHome(
            @RequestParam("limit") int limit, @RequestParam("offset") int offset) {
        var result = productService.getProductForHome(limit, offset);

        return ResponseEntity.ok(
                ApiResponse.builder().data(result).message("get product successfully").success(true).build());
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse> getDetailOfProduct(@PathVariable String id) {
        var result = productService.getProductDetail(id);

        return ResponseEntity.ok(
                ApiResponse.builder().data(result).message("get product detail successfully").build());
    }

    @PostMapping("/test")
    public ResponseEntity<ApiResponse> createProduct(@Valid @ModelAttribute CreateCategoryRequest request) {//modelAttribute
        System.out.println(request);
        return ResponseEntity.ok(
                ApiResponse.builder().message("get product detail successfully").build());
    }
}
