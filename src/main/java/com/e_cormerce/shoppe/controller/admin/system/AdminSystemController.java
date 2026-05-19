package com.e_cormerce.shoppe.controller.admin.system;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.admin.AdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminSystemController {
  AdminService adminService;

  @GetMapping("/overview-order-product")
  public ResponseEntity<ApiResponse> getOverviewOrderProductForAdmin(
      @RequestParam(required = false) Integer days) {

    var result = adminService.getOverviewOrderProductForAdmin(days);

    return ResponseEntity.ok(
        ApiResponse.builder()
            .success(true)
            .message("Get system overview successfully")
            .data(result)
            .build());
  }

  @GetMapping("/overview-system")
  public ResponseEntity<ApiResponse> getOverviewSystemForAdmin(
      @RequestParam(required = false) Integer days) {

    var result = adminService.getOverViewSystemForAdmin(days);

    return ResponseEntity.ok(
        ApiResponse.builder()
            .success(true)
            .message("Get system overview successfully")
            .data(result)
            .build());
  }
}
