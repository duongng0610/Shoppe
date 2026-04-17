package com.e_cormerce.shoppe.event.order.listener;

import com.e_cormerce.shoppe.enums.notification.NotificationType;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.event.order.event.*;
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
public class OrderListener {

    NotificationService notificationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {

        notificationService.createNotification(
                event.getClient(),
                NotificationType.ORDER,
                event.getOrderId(),
                "Đặt thành công đơn hàng",
                "Đơn hàng " + event.getOrderId() + " đã được đặt thành công.",
                event.getVariant().getThumbnail());

        notificationService.createNotification(
                event.getSeller(),
                NotificationType.ORDER,
                event.getOrderId(),
                "Sản phẩm đang chờ xác nhận",
                "Bạn có một đơn hàng mới từ " + event.getClient().getUsername(),
                event.getVariant().getThumbnail());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderApproved(OrderApprovedEvent event) {

        notificationService.createNotification(
                event.getClient(),
                NotificationType.ORDER,
                event.getOrderId(),
                "Cập nhật đơn hàng",
                "Đơn hàng " + event.getOrderId() + " đã được xác nhận.",
                event.getVariant().getThumbnail());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderApproved(OrderCancelledEvent event) {
        String status = event.getOrderStatus();

        if (status.equals(OrderStatus.CANCELLED_BY_CLIENT.toString())) {
            notificationService.createNotification(
                    event.getClient(),
                    NotificationType.ORDER,
                    event.getOrderId(),
                    "Cập nhật đơn hàng",
                    "Đơn hàng " + event.getOrderId() + " đã bị huỷ bởi bạn.",
                    event.getVariant().getThumbnail());

        } else {
            notificationService.createNotification(
                    event.getClient(),
                    NotificationType.ORDER,
                    event.getOrderId(),
                    "Cập nhật đơn hàng",
                    "Đơn hàng " + event.getOrderId() + " đã bị hủy bởi shop.",
                    event.getVariant().getThumbnail());
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderShipped(OrderShippedEvent event) {

        notificationService.createNotification(
                event.getClient(),
                NotificationType.ORDER,
                event.getOrderId(),
                "Cập nhật đơn hàng",
                "Đơn hàng " + event.getOrderId() + " đã bắt đầu được giao.",
                event.getVariant().getThumbnail());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUpdateTracking(OrderTrackingUpdatedEvent event) {
        String status = event.getStatus();

        if (status.equals(OrderStatus.ARRIVED.toString())) {
            notificationService.createNotification(
                    event.getClient(),
                    NotificationType.ORDER,
                    event.getOrderId(),
                    "Cập nhật đơn hàng",
                    "Đơn hàng " + event.getOrderId() + " đã giao đến nơi tại " + event.getAddress(),
                    null);
        } else if (status.equals(OrderStatus.SHIPPING.toString())) {
            notificationService.createNotification(
                    event.getClient(),
                    NotificationType.ORDER,
                    event.getOrderId(),
                    "Đơn hàng đang giao",
                    "Đơn hàng " + event.getOrderId() + " đang ở vị trí " + event.getAddress(),
                    null);
        }
    }
}
