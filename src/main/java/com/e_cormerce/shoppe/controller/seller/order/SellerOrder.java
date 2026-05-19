package com.e_cormerce.shoppe.controller.seller.order;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.order.OrderService;
import com.e_cormerce.shoppe.service.seller.SellerService;
import jakarta.servlet.http.HttpServletResponse;
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
    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse> approveOrder(@PathVariable String id) {
        sellerService.approveOrder(id);
        return ResponseEntity.ok()
                .body(ApiResponse.builder().message("approve order successfully").success(true).build());
    }

    @PreAuthorize("hasAuthority('PERMISSION_CANCEL_ORDER_BY_SELLER')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable String id) {
        sellerService.cancelOrder(id);
        return ResponseEntity.ok()
                .body(ApiResponse.builder().message("cancel order successfully").success(true).build());
    }

    @PreAuthorize("hasAuthority('PERMISSION_SHIP_ORDER')")
    @PutMapping("/{id}/ship")
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

    @GetMapping("/{orderId}/shipment-state")
    public ResponseEntity<ApiResponse> getCurrentTracking(@PathVariable String orderId) {
        var result = orderService.getOrderShipInfo(orderId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .data(result)
                        .message("get order tracking successfully")
                        .success(true)
                        .build());
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getOrderSystem(
            @RequestParam Integer limit, @RequestParam Integer offset) {
        var result = orderService.getSystemOrder(limit, offset);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .data(result)
                        .message("get orders successfully")
                        .success(true)
                        .build());
    }

    // =====================================
    // SELLER
    // =====================================

    @GetMapping("revenue")
    public ResponseEntity<ApiResponse> getSellerRevenue(
            @RequestParam String sellerId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer days) {

        var result = orderService.getSellerRevenue(sellerId, status, days);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .data(result)
                        .message("get order revenue successfully")
                        .success(true)
                        .build());
    }


    @GetMapping("/export_csv")
    public void getExportCsv(HttpServletResponse response) {
        sellerService.exportOrderCsv(response);

    }
}
