// package com.e_cormerce.shoppe.event.listener.seller_stat;
//
// import com.e_cormerce.shoppe.event.order.*;
// import com.e_cormerce.shoppe.repository.seller.SellerStatRepository;
// import java.math.BigDecimal;
// import lombok.AccessLevel;
// import lombok.RequiredArgsConstructor;
// import lombok.experimental.FieldDefaults;
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.stereotype.Component;
// import org.springframework.transaction.event.TransactionPhase;
// import org.springframework.transaction.event.TransactionalEventListener;
//
// @Component
// @RequiredArgsConstructor
// @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// public class SellerOrderStatListener {
//  SellerStatRepository sellerStatRepository;
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleProductCreated(OrderCreated event) {
//    var seller = event.getSeller();
//    sellerStatRepository.createOrder(seller.getId());
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleProductApproved(OrderApproved event) {
//    var seller = event.getSeller();
//    sellerStatRepository.approveOrder(seller.getId());
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleOrderShipping(OrderShipping event) {
//    var seller = event.getSeller();
//    sellerStatRepository.shippingOrder(seller.getId());
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleOrderCancelledByClient(OrderCancelledByClient event) {
//    var seller = event.getSeller();
//    sellerStatRepository.cancelOrderByClient(seller.getId());
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleOrderCancelledBySeller(OrderCancelledBySeller event) {
//    var seller = event.getSeller();
//    sellerStatRepository.cancelOrderBySeller(seller.getId());
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleOrderPaid(OrderPayment event) {
//    if (!event.isSuccess()) {
//      return;
//    }
//    var order = event.getOrder();
//    sellerStatRepository.completeOrder(
//        event.getSellerId(),
//        order.getPriceEach().multiply(BigDecimal.valueOf(order.getQuantity())));
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleOrderFailedDelivery(OrderFailedDelivery event) {
//    var seller = event.getSeller();
//    sellerStatRepository.failDelivery(seller.getId());
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleOrderRequireRefund(OrderRequireRefund event) {
//    var seller = event.getSeller();
//    sellerStatRepository.requestRefund(seller.getId());
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleOrderRefunded(OrderRefunded event) {
//    var seller = event.getSeller();
//    sellerStatRepository.refundOrder(seller.getId());
//  }
// }
