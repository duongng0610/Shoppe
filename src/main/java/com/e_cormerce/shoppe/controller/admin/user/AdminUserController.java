package com.e_cormerce.shoppe.controller.admin.user;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.admin.AdminService;
import com.e_cormerce.shoppe.service.category.CategoryService;
import com.e_cormerce.shoppe.service.transaction.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserController {
    CategoryService categoryService;
    AdminService adminService;
    TransactionService transactionService;

    @PreAuthorize("hasAuthority('PERMISSION_GET_USERS_MANAGEMENT')")
    @GetMapping(path = "/clients")
    public ResponseEntity<ApiResponse> getClientInfo(
            @RequestParam int limit, @RequestParam int offset) {
        var clients = adminService.getClientInfo(limit, offset);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("get client info successfully")
                        .data(clients)
                        .build());
    }

    @PreAuthorize("hasAuthority('PERMISSION_GET_USER_DETAILS_MANAGEMENT')")
    @GetMapping(path = "/users/{id}/detail")
    public ResponseEntity<ApiResponse> getClientDetailInfo(@PathVariable String id) {
        var user = adminService.getInfoUser(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("get user detail info successfully")
                        .data(user)
                        .build());
    }

    @PreAuthorize("hasAuthority('PERMISSION_GET_USERS_MANAGEMENT')")
    @GetMapping(path = "/sellers")
    public ResponseEntity<ApiResponse> getRegisteredSellerInfo(
            @RequestParam int limit, @RequestParam int offset) {
        var sellers = adminService.getSellerInfo(limit, offset);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("get registered seller info successfully")
                        .data(sellers)
                        .build());
    }

    @GetMapping("/users/{userId}/transactions")
    public ResponseEntity<ApiResponse> getTransactionOfUser(
            @PathVariable String userId, @RequestParam Integer limit, @RequestParam Integer offset) {

        var result = transactionService.getTransactionOfUser(userId, limit, offset);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Get transaction user views successfully")
                        .data(result)
                        .build());
    }


    // User Ban/Unban Endpoints
    @PreAuthorize("hasAuthority('PERMISSION_LOCK_USER')")
    @PatchMapping(path = "/users/{userId}/ban")
    public ResponseEntity<ApiResponse> banUser(@PathVariable String userId) {
        adminService.banUser(userId);
        return ResponseEntity.ok(
                ApiResponse.builder().success(true).message("ban user successfully").build());
    }

    @PreAuthorize("hasAuthority('PERMISSION_UNLOCK_USER')")
    @PatchMapping(path = "/users/{userId}/unban")
    public ResponseEntity<ApiResponse> unbanUser(@PathVariable String userId) {
        adminService.unbanUser(userId);
        return ResponseEntity.ok(
                ApiResponse.builder().success(true).message("unban user successfully").build());
    }
}
