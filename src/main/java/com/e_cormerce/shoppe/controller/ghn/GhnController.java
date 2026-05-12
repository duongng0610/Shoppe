package com.e_cormerce.shoppe.controller.ghn;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.service.order.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ghn/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GhnController {
    OrderService orderService;


    //  @PreAuthorize("hasAuthority('PERMISSION_ACCEPT_ORDER')")
    @PatchMapping("/{id}/success")
    public ResponseEntity<ApiResponse> updateOrderDeliverSuccess(@PathVariable String id) {
        orderService.updateDeliverOrder(id, OrderStatus.DELIVERED);
        return ResponseEntity.ok()
                .body(ApiResponse.builder().message("update order : delivery success").success(true).build());
    }

    //  @PreAuthorize("hasAuthority('PERMISSION_ACCEPT_ORDER')")
    @PatchMapping("/{id}/fail")
    public ResponseEntity<ApiResponse> updateOrderDeliverFail(@PathVariable String id) {
        orderService.updateDeliverOrder(id, OrderStatus.FAILED_DELIVERY);
        return ResponseEntity.ok()
                .body(ApiResponse.builder().message("update order : fail success").success(true).build());
    }

}
