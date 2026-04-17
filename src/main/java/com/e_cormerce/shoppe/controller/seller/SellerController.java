package com.e_cormerce.shoppe.controller.seller;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.seller.SellerInfoResponse;
import com.e_cormerce.shoppe.service.order.OrderService;
import com.e_cormerce.shoppe.service.order.OrderTrackingService;
import com.e_cormerce.shoppe.service.seller.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerController {
    SellerService sellerService;
    OrderService orderService;
    OrderTrackingService orderTrackingService;

    @GetMapping("/{id}/info")
    public ResponseEntity<ApiResponse<SellerInfoResponse>> getSeller(@PathVariable String id) {
        var res = sellerService.getSeller(id);
        return ResponseEntity.ok(ApiResponse.<SellerInfoResponse>builder().data(res).build());
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<ApiResponse> getProductsOfSeller(
            @PathVariable String id, @RequestParam Integer limit, @RequestParam Integer offset) {
        var res = sellerService.getProductCardsBySeller(id, limit, offset);
        return ResponseEntity.ok(ApiResponse.builder().data(res).build());
    }


}
