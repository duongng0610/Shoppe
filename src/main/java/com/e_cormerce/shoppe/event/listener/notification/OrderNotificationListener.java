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
                "Đơn hàng " + event.getOrder().getProductName() + " đã được đặt thành công.",
                event.getOrder().getThumbnail());

        notificationService.createNotification(
                event.getSeller().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Sản phẩm đang chờ xác nhận",
                "Bạn có một đơn hàng mới từ " + event.getClient().getUsername(),
                event.getOrder().getThumbnail());

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderApproved(OrderApproved event) {
        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Cập nhật đơn hàng",
                "Đơn hàng " + event.getOrder().getProductName() + " đã được xác nhận.",
                event.getOrder().getThumbnail());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCancelOrderByClient(OrderCancelledByClient event) {
        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Cập nhật đơn hàng",
                "Bạn đã hủy đơn hàng " + event.getOrder().getProductName() + " đã bị huỷ.",
                event.getOrder().getThumbnail());


        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Cập nhật đơn hàng",
                "Đơn hàng " + event.getOrder().getProductName() + " đã bị hủy bởi shop.",
                event.getOrder().getThumbnail());

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderShipping(OrderArrived event) {

        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Cập nhật đơn hàng",
                "Đơn hàng " + event.getOrder().getProductName() + " đã bắt đầu được giao.",
                event.getOrder().getThumbnail());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUpdateTracking(OrderLocationUpdated event) {


        notificationService.createNotification(
                event.getClient().getId(),
                NotificationType.ORDER,
                event.getOrder().getId(),
                "Cập nhật vị trí đơn hàng",
                "Đơn hàng " + event.getOrder().getProductName() + " đã giao đến nơi tại " + event.getAddress(),
                event.getOrder().getThumbnail());


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
                event.getOrder().getThumbnail());
    }

}
