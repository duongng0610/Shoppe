package com.e_cormerce.shoppe.controller.admin.transaction;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.transaction.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminTransactionController {
    TransactionService transactionService;

    @GetMapping("")
    public ResponseEntity<ApiResponse>
    getTransactionViews(

            @RequestParam Integer limit,

            @RequestParam Integer offset
    ) {

        var result =
                transactionService
                        .getTransactionUserViews(
                                limit,
                                offset
                        );

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(
                                "Get transaction user views successfully"
                        )
                        .data(result)
                        .build()
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse>
    getTransactionOfUser(
            @PathVariable String userId,

            @RequestParam Integer limit,

            @RequestParam Integer offset
    ) {

        var result =
                transactionService
                        .getTransactionOfUser(
                                userId,
                                limit,
                                offset
                        );

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(
                                "Get transaction user views successfully"
                        )
                        .data(result)
                        .build()
        );
    }
}
