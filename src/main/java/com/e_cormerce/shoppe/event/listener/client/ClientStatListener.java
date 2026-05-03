package com.e_cormerce.shoppe.event.listener.client;

import com.e_cormerce.shoppe.event.order.*;
import com.e_cormerce.shoppe.repository.client.ClientStatRepository;
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
public class ClientStatListener {
  ClientStatRepository clientStatRepository;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleProductCreated(OrderCreated event) {
    var client = event.getClient();
    clientStatRepository.createOrder(client.getId());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleProductApproved(OrderApproved event) {
    var client = event.getClient();
    clientStatRepository.approveOrder(client.getId());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderShipping(OrderShipping event) {
    var client = event.getClient();
    clientStatRepository.shippingOrder(client.getId());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderCancelledByClient(OrderCancelledByClient event) {
    var client = event.getClient();
    clientStatRepository.cancelOrder(client.getId());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderPaid(OrderPayment event) {
    if (!event.isSuccess()) {
      return;
    }
    var client = event.getClient();
    clientStatRepository.completeOrder(client.getId(), event.getOrder().getTotalPrice());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderFailedDelivery(OrderFailedDelivery event) {
    var client = event.getClient();
    clientStatRepository.failDelivery(client.getId());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderRequireRefund(OrderRequireRefund event) {
    var client = event.getClient();
    clientStatRepository.requestRefund(client.getId());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderRefunded(OrderRefunded event) {
    var client = event.getClient();
    clientStatRepository.refundOrder(client.getId());
  }
}
