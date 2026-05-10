package com.e_cormerce.shoppe.controller.user.account;

import com.e_cormerce.shoppe.dto.request.account.ChangePasswordRequest;
import com.e_cormerce.shoppe.dto.request.account.ChangeUserProfileRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.account.AccountService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
  AccountService accountService;

  @PatchMapping("/password")
  public ResponseEntity<ApiResponse> changePassword(
      @RequestBody @Valid ChangePasswordRequest request) {
    accountService.changePassword(request);
    return ResponseEntity.ok()
        .body(ApiResponse.builder().message("change password successfully").success(true).build());
  }

  @PutMapping(value = "/profile", consumes = "multipart/form-data")
  public ResponseEntity<ApiResponse> updateProfile(
      @RequestBody @Valid ChangeUserProfileRequest request) {
    var res = accountService.updateProfile(request);
    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .message("update profile successfully")
                .data(res)
                .success(true)
                .build());
  }

  @GetMapping("/profile")
  public ResponseEntity<ApiResponse> getProfile() {
    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .data(accountService.getUserProfile())
                .message("profile successfully")
                .build());
  }
}
