package com.e_cormerce.shoppe.event;

import com.e_cormerce.shoppe.enums.notification.NotificationType;
import com.e_cormerce.shoppe.service.notification.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderNotificationListener {

  NotificationService notificationService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderCreated(OrderCreatedEvent event) {

    notificationService.createNotification(
        event.getClient(),
        NotificationType.ORDER,
        event.getOrder().getId(),
        "Đặt thành công đơn hàng",
        "Đơn hàng " + event.getOrder().getId() + " đã được đặt thành công.",
        event.getVariant().getThumbnail());

    notificationService.createNotification(
        event.getSeller(),
        NotificationType.ORDER,
        event.getOrder().getId(),
        "Sản phẩm đang chờ xác nhận",
        "Bạn có một đơn hàng mới từ " + event.getClient().getUsername(),
        event.getVariant().getThumbnail());
  }
}
