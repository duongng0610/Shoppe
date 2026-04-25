package com.e_cormerce.shoppe.configuration;

import com.e_cormerce.shoppe.enums.order.OrderPaymentStatus;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.enums.order.ReservationStatus;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import com.e_cormerce.shoppe.repository.order.OrderReservationRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CronJob {
    OrderReservationRepository orderReservationRepository;
    OrderRepository orderRepository;
    VariantRepository variantRepository;

    @Scheduled(fixedRate = 30000)
    public void handleCheckExpiredReservation() {
        var expiredReservations = orderReservationRepository.findExpired(ReservationStatus.ACTIVE, LocalDateTime.now());
        System.out.println("Checking expired reservation: " + expiredReservations.size());
        expiredReservations.forEach(reservation -> {
            var order = reservation.getOrder();
            order.setStatus(OrderStatus.CANCELLED_BY_CLIENT);
            order.setPaymentStatus(OrderPaymentStatus.EXPIRED);

            var variant = reservation.getVariant();
            variant.setReserved(variant.getReserved() - reservation.getQuantity());

            reservation.setStatus(ReservationStatus.EXPIRED);

            orderReservationRepository.save(reservation);
            orderRepository.save(order);
            variantRepository.save(variant);
        });
    }
}
