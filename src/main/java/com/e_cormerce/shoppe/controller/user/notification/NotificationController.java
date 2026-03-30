package com.e_cormerce.shoppe.controller.user.notification;

import com.e_cormerce.shoppe.dto.request.notification.ReadNotificationRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.entity.notification.Notification;
import com.e_cormerce.shoppe.service.notification.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationService;
    // 🔥 1. Đếm số chưa đọc
    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getCount() {
        return ResponseEntity.ok(
                ApiResponse.builder().data( notificationService.countUnReadNotifications()).build());

    }

    // 🔥 2. Lấy danh sách notification (có phân trang)
    @GetMapping
    public ResponseEntity<ApiResponse> getUnread(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return ResponseEntity.ok(
              ApiResponse.builder().data(notificationService.getNotifications(limit,offset)).build()
        );
    }

    // 🔥 3. Mark as read
    @PatchMapping("/read")
    public ResponseEntity<ApiResponse> read(@RequestBody ReadNotificationRequest request) {
        notificationService.readNotifications(request.getIds());
        return ResponseEntity.ok(
                ApiResponse.builder().message("Doc thong bao thanh cong").build());
    }
}
