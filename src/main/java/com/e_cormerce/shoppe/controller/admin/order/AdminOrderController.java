package com.e_cormerce.shoppe.controller.admin.order;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.order.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderController {
    OrderService orderService;
    // =====================================
    // ADMIN
    // =====================================

    @GetMapping("revenue")
    public ResponseEntity<ApiResponse> getAdminRevenue(

            @RequestParam(required = false)
            String status,

            @RequestParam(required = false)
            Integer days
    ) {

        var result = orderService.getAdminRevenue(
                status,
                days
        );

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(
                                "Get system revenue successfully"
                        )
                        .data(result)
                        .build()
        );
    }

    @GetMapping("seller/id/revenue")
    public ResponseEntity<ApiResponse> getSellerRevenue(
            @PathVariable String id,
            @RequestParam(required = false)
            String status,

            @RequestParam(required = false)
            Integer days
    ) {

        var result = orderService.getSellerRevenue(
                id,
                status,
                days
        );

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(
                                "Get seller revenue successfully"
                        )
                        .data(result)
                        .build()
        );
    }
}
