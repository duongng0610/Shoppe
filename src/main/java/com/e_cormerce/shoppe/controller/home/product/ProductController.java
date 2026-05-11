package com.e_cormerce.shoppe.controller.home.product;

import com.e_cormerce.shoppe.dto.request.product.CreateProductReviewRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
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
        ApiResponse.builder()
            .data(result)
            .message("get product successfully")
            .success(true)
            .build());
  }

  @GetMapping("/{id}/details")
  public ResponseEntity<ApiResponse> getDetailOfProduct(@PathVariable String id) {
    var result = productService.getProductDetail(id);

    return ResponseEntity.ok(
        ApiResponse.builder().data(result).message("get product detail successfully").build());
  }

  @GetMapping("/{productId}/reviews")
  public ResponseEntity<ApiResponse> getReviewsOfProduct(
      @PathVariable String productId,
      @RequestParam(value = "limit") int limit,
      @RequestParam(value = "offset") int offset) {
    var result = productService.getReviewOfProduct(productId, limit, offset);

    return ResponseEntity.ok(
        ApiResponse.builder()
            .data(result)
            .message("get reviews successfully")
            .success(true)
            .build());
  }
}
