package com.e_cormerce.shoppe.event.listener;

import com.e_cormerce.shoppe.event.order.OrderCreated;
import com.e_cormerce.shoppe.event.order.OrderPayment;
import com.e_cormerce.shoppe.event.product.ProductCreated;
import com.e_cormerce.shoppe.event.system.ClientRegistered;
import com.e_cormerce.shoppe.event.system.SellerRegistered;
import com.e_cormerce.shoppe.event.system.UserVisited;
import com.e_cormerce.shoppe.repository.analytic.SystemDailyRepository;
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
public class SystemDailyListener {
    SystemDailyRepository systemDailyRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegistered(ClientRegistered event) {
        systemDailyRepository.increaseNewClient(event.getDate().toLocalDate());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserLoggedIn(SellerRegistered event) {
        systemDailyRepository.increaseNewSeller(event.getDate().toLocalDate());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserVisited(UserVisited event) {
        systemDailyRepository.increaseVisit(event.getDate().toLocalDate());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreated(ProductCreated event) {
        var product = event.getProduct();
        systemDailyRepository.increaseNewProduct(product.getCreatedAt().toLocalDate());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderPaid(OrderPayment event) {
        if (event.isSuccess()) {
            systemDailyRepository.increaseTransactionAmount(event.getOrder().getTotalPrice(), event.getPaymentDate().toLocalDate());
        } else {
            systemDailyRepository.increaseFailedTransactionAmount(event.getOrder().getTotalPrice(), event.getPaymentDate().toLocalDate());
        }

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreated event) {
        systemDailyRepository.increaseNewOrder(event.getOrder().getCreatedAt().toLocalDate());

    }

}