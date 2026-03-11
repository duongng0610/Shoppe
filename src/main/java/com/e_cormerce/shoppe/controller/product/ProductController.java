package com.e_cormerce.shoppe.controller.product;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
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
        ApiResponse.builder().data(result).message("get product successfully").build());
  }
}
