package com.e_cormerce.shoppe.controller.client.product;

import com.e_cormerce.shoppe.dto.request.product.CreateProductReviewRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("client/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientProductController {
  ProductService productService;

  @PostMapping("/reviews")
  @PreAuthorize("hasAuthority('PERMISSION_REVIEW_PRODUCT')")
  public ResponseEntity<ApiResponse> createReview(
      @Valid @RequestBody CreateProductReviewRequest request) {
    productService.createReview(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.builder().message("review created successfully").success(true).build());
  }
}
