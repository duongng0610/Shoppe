package com.e_cormerce.shoppe.controller.seller.system;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.seller.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerSystemController {
  SellerService sellerService;

  @GetMapping("/overview-order-product")
  public ResponseEntity<ApiResponse> getOverviewOrderProduct(
      @RequestParam(required = false) Integer days) {

    var result = sellerService.getOverviewOrderProduct(days);

    return ResponseEntity.ok(
        ApiResponse.builder()
            .success(true)
            .message("Get overview order product successfully")
            .data(result)
            .build());
  }
}
