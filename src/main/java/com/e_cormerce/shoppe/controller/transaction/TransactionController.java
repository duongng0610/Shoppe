package com.e_cormerce.shoppe.controller.transaction;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.service.payment.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    PaymentService paymentService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTransaction(@PathVariable String id) throws AppException {
        return ResponseEntity.ok(ApiResponse.builder().data(paymentService.getTransaction(id)).success(true).build());
    }
}
