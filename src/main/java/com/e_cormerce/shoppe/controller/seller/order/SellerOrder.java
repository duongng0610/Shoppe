package com.e_cormerce.shoppe.controller.seller.order;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.order.GetOrderDetailResponse;
import com.e_cormerce.shoppe.service.order.OrderService;
import com.e_cormerce.shoppe.service.seller.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerOrder {
  SellerService sellerService;
  OrderService orderService;

  @PreAuthorize("hasAuthority('PERMISSION_ACCEPT_ORDER')")
  @PatchMapping("/{id}/approve")
  public ResponseEntity<ApiResponse> approveOrder(@PathVariable String id) {
    sellerService.approveOrder(id);
    return ResponseEntity.ok()
        .body(ApiResponse.builder().message("approve order successfully").success(true).build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_CANCEL_ORDER_BY_SELLER')")
  @PatchMapping("/{id}/cancel")
  public ResponseEntity<ApiResponse> cancelOrder(@PathVariable String id) {
    sellerService.cancelOrder(id);
    return ResponseEntity.ok()
        .body(ApiResponse.builder().message("cancel order successfully").success(true).build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_SHIP_ORDER')")
  @PatchMapping("/{id}/ship")
  public ResponseEntity<ApiResponse> shipOrder(@PathVariable String id) {
    var res = sellerService.shipOrder(id);
    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .message("ship order successfully")
                .data(res)
                .success(true)
                .build());
  }

  @GetMapping("")
  @PreAuthorize("hasAuthority('PERMISSION_VIEW_SELLER_ORDERS')")
  public ResponseEntity<ApiResponse<GetOrderDetailResponse>> getSellerOrders() {
    var result = orderService.getOrdersBySeller();
    return ResponseEntity.ok(
        ApiResponse.<GetOrderDetailResponse>builder()
            .data(result)
            .message("get orders successfully")
            .success(true)
            .build());
  }
}
