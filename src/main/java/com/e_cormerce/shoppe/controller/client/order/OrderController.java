package com.e_cormerce.shoppe.controller.client.order;

import com.e_cormerce.shoppe.dto.request.order.CreateOrderRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.order.CreateOrderResponse;
import com.e_cormerce.shoppe.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("client/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
  OrderService orderService;

  @PostMapping()
  @PreAuthorize("hasAuthority('PERMISSION_CREATE_ORDER')")
  public ResponseEntity<ApiResponse<CreateOrderResponse>> create(
      @RequestBody @Valid CreateOrderRequest request) {
    var result = orderService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.<CreateOrderResponse>builder()
                .success(true)
                .message("create order successfully")
                .data(result)
                .build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_CANCEL_ORDER_BY_CLIENT')")
  @PatchMapping("/{id}/cancel")
  public ResponseEntity<ApiResponse> cancelOrder(@PathVariable String id) {
    orderService.cancelOrder(id);
    return ResponseEntity.ok()
        .body(ApiResponse.builder().message("cancel order successfully").success(true).build());
  }
}
