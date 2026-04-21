package com.e_cormerce.shoppe.controller.client.order;

import com.e_cormerce.shoppe.dto.request.order.CreateOrderRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.order.CreateOrderResponse;
import com.e_cormerce.shoppe.dto.response.order.GetCurrentTrackingResponse;
import com.e_cormerce.shoppe.dto.response.order.GetOrderDetailResponse;
import com.e_cormerce.shoppe.service.order.OrderService;
import com.e_cormerce.shoppe.service.order.OrderTrackingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
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
    OrderTrackingService orderTrackingService;

    @PostMapping()
    @PreAuthorize("hasAuthority('PERMISSION_CREATE_ORDER')")
    public ResponseEntity<ApiResponse<CreateOrderResponse>> create(
            @RequestBody @Valid CreateOrderRequest request) throws JsonProcessingException {
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

    @PreAuthorize("hasAuthority('PERMISSION_CANCEL_ORDER_BY_CLIENT')")
    @GetMapping("/{orderId}/trackings/current")
    public ResponseEntity<ApiResponse<GetCurrentTrackingResponse>> getCurrentTracking(
            @PathVariable String orderId) {
        var result = orderTrackingService.getCurrentTracking(orderId);
        return ResponseEntity.ok(
                ApiResponse.<GetCurrentTrackingResponse>builder()
                        .data(result)
                        .message("get order tracking successfully")
                        .success(true)
                        .build());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_CLIENT_ORDERS')")
    public ResponseEntity<ApiResponse<GetOrderDetailResponse>> getClientOrders() {
        var result = orderService.getOrdersByClient();
        return ResponseEntity.ok(
                ApiResponse.<GetOrderDetailResponse>builder()
                        .data(result)
                        .message("get orders successfully")
                        .success(true)
                        .build());
    }

    @PostMapping("{id}/payment")
    public String submidOrder(@PathVariable String id,
                              HttpServletRequest request) {
        return orderService.getUrlPaymentByOrder(id, request);
    }
}
