package com.e_cormerce.shoppe.controller.seller;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.product.ProductService;
import com.e_cormerce.shoppe.service.seller.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller/public")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerPublicController {
    SellerService sellerService;
    ProductService productService;

    @GetMapping("/{id}/products")
    public ResponseEntity<ApiResponse> getProductsOfSeller(
            @PathVariable String id,
            @RequestParam Integer limit,
            @RequestParam Integer offset) {
        var res = productService.getProductsInSellerPage(id, limit, offset);
        return ResponseEntity.ok(ApiResponse.builder().data(res).build());
    }

    @GetMapping("/{sellerId}/info")
    public ResponseEntity<ApiResponse> getSellerInformation(@PathVariable String sellerId) {

        var result = sellerService.getSellerInformation(sellerId);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Get seller information successfully")
                        .data(result)
                        .build());
    }
}
