package com.e_cormerce.shoppe.event.listener.notification;

import com.e_cormerce.shoppe.enums.notification.NotificationType;
import com.e_cormerce.shoppe.event.product.*;
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
public class ProductNotificationListener {

  NotificationService notificationService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleProductCreated(ProductCreated event) {
    var product = event.getProduct();
    var seller = event.getSeller();
    notificationService.createNotification(
        seller.getId(),
        NotificationType.PRODUCT,
        product.getId(),
        "Yêu cầu tạo sản phẩm",
        "Yêu cầu tạo sản phẩm thành công",
        product.getThumbnail());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleProductApproved(ProductApproved event) {
    var product = event.getProduct();
    var seller = event.getSeller();
    notificationService.createNotification(
        seller.getId(),
        NotificationType.PRODUCT,
        product.getId(),
        "Phản hồi tạo sản phẩm",
        "Sản phẩm đã được chấp nhận",
        product.getThumbnail());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleProductRejected(ProductRejected event) {
    var product = event.getProduct();
    var seller = event.getSeller();

    notificationService.createNotification(
        seller.getId(),
        NotificationType.PRODUCT,
        product.getId(),
        "Phản hồi tạo sản phẩm",
        "Sản phẩm đã bị từ chối",
        product.getThumbnail());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleProductBanned(ProductBanned event) {
    var product = event.getProduct();
    var seller = event.getSeller();

    notificationService.createNotification(
        seller.getId(),
        NotificationType.PRODUCT,
        product.getId(),
        "Khóa sản phẩm",
        "Sản phẩm đã bị khóa",
        product.getThumbnail());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleProductUnlocked(ProductUnlocked event) {
    var product = event.getProduct();
    var seller = event.getSeller();

    notificationService.createNotification(
        seller.getId(),
        NotificationType.PRODUCT,
        product.getId(),
        "Mở khóa sản phẩm",
        "Sản phẩm đã được mở khóa",
        product.getThumbnail());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleProductHidden(ProductHidden event) {
    var product = event.getProduct();
    var seller = event.getSeller();

    notificationService.createNotification(
        seller.getId(),
        NotificationType.PRODUCT,
        product.getId(),
        "Ẩn sản phẩm",
        "Sản phẩm đã được ẩn",
        product.getThumbnail());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleProductUnhidden(ProductUnhidden event) {
    var product = event.getProduct();
    var seller = event.getSeller();

    notificationService.createNotification(
        seller.getId(),
        NotificationType.PRODUCT,
        product.getId(),
        "Hiển thị sản phẩm",
        "Sản phẩm đã được hiển thị lại",
        product.getThumbnail());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleProductDeleted(ProductDeleted event) {
    var product = event.getProduct();
    var seller = event.getSeller();
    notificationService.createNotification(
        seller.getId(),
        NotificationType.PRODUCT,
        product.getId(),
        "Xóa sản phẩm",
        "Sản phẩm đã được xóa",
        product.getThumbnail());
  }
}
