package com.e_cormerce.shoppe.event.listener.notification;

import com.e_cormerce.shoppe.enums.notification.NotificationType;
import com.e_cormerce.shoppe.event.order.*;
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
    public void handleOrderCreated(OrderCreated event) {
        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Đặt thành công đơn hàng",
                "Đơn hàng " + event.getOrder().getOrderName() + " đã được đặt thành công.",
                event.getOrder().getOrderThumbnail());

        notificationService.createNotification(
                event.getSeller().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Sản phẩm đang chờ xác nhận",
                "Bạn có một đơn hàng mới từ " + event.getClient().getUsername(),
                event.getOrder().getOrderThumbnail());

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderApproved(OrderApproved event) {
        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Cập nhật đơn hàng",
                "Đơn hàng " + event.getOrder().getOrderName() + " đã được xác nhận.",
                event.getOrder().getOrderThumbnail());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCancelOrderByClient(OrderCancelledByClient event) {
        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Cập nhật đơn hàng",
                "Bạn đã hủy đơn hàng " + event.getOrder().getOrderName() + " đã bị huỷ.",
                event.getOrder().getOrderThumbnail());


        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Cập nhật đơn hàng",
                "Đơn hàng " + event.getOrder().getOrderName() + " đã bị hủy bởi shop.",
                event.getOrder().getOrderThumbnail());

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderShipping(OrderArrived event) {

        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Cập nhật đơn hàng",
                "Đơn hàng " + event.getOrder().getOrderName() + " đã bắt đầu được giao.",
                event.getOrder().getOrderThumbnail());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUpdateTracking(OrderLocationUpdated event) {


        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Cập nhật vị trí đơn hàng",
                "Đơn hàng " + event.getOrder().getOrderName() + " đã giao đến nơi tại " + event.getAddress(),
                event.getOrder().getOrderThumbnail());


    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderArrived(OrderLocationUpdated event) {
        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Đơn hàng đã đến nơi",
                "Đơn hàng đã tới địa điểm giao",
                event.getOrder().getOrderThumbnail());
    }


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderPayment(OrderPayment event) {
        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Thanh toán đơn hàng",
                event.isSuccess() ? "Thanh toán đơn hàng: " + event.getOrder().getId() + " thành công " : "Thanh toán đơn hàng: " + event.getOrder().getId() + " thất bại ",
                event.getOrder().getOrderThumbnail());
        if (event.isSuccess()) {
            notificationService.createNotification(
                    event.getClient().getId(),
                    NotificationType.ORDER,
                    event.getOrder().getId(),
                    "Thanh toán đơn hàng",
                    event.getClient().getUsername() + " thanh toán đơn hàng: " + event.getOrder().getId() + " thành công ",
                    event.getOrder().getOrderThumbnail());
        }
    }

}
