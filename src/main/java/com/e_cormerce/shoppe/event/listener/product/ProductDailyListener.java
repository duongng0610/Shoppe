package com.e_cormerce.shoppe.event.listener.product;

import com.e_cormerce.shoppe.event.order.OrderCreated;
import com.e_cormerce.shoppe.event.order.OrderPayment;
import com.e_cormerce.shoppe.repository.analytic.ProductVariantDailyRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import java.math.BigDecimal;
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
public class ProductDailyListener {
  ProductVariantDailyRepository productVariantDailyRepository;

  VariantRepository variantRepository;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderCreated(OrderCreated event) {
    var seller = event.getSeller();
    productVariantDailyRepository.increaseOrder(
        event.getVariantId(),
        event.getProductId(),
        event.getOrder().getQuantity(),
        event.getOrder().getCreatedAt().toLocalDate());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderPaid(OrderPayment event) {
    if (!event.isSuccess()) {
      return;
    }
    var order = event.getOrder();
    var productId = variantRepository.getProductId(event.getVariantId());
    productVariantDailyRepository.increaseRevenue(
        event.getVariantId(),
        productId,
        order.getPriceEach().multiply(BigDecimal.valueOf(order.getQuantity())),
        event.getPaymentDate().toLocalDate());
  }
}
