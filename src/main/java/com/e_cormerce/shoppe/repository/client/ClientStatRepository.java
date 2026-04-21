package com.e_cormerce.shoppe.repository.client;

import com.e_cormerce.shoppe.entity.client.ClientStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface ClientStatRepository extends JpaRepository<ClientStat, Integer> {
    @Modifying
    @Transactional
    @Query("""
                UPDATE ClientStat c
                SET c.totalOrders = c.totalOrders + 1 , c.pendingOrders = c.pendingOrders + 1
                WHERE c.client.id = :clientId
            """)
    void createOrder(String clientId);

    @Modifying
    @Transactional
    @Query("""
                UPDATE ClientStat c
                SET c.pendingOrders = c.pendingOrders - 1,
                    c.approvedOrders = c.approvedOrders + 1
                WHERE c.client.id = :clientId
            """)
    void approveOrder(String clientId);

    @Modifying
    @Transactional
    @Query("""
                UPDATE ClientStat c
                SET c.approvedOrders = c.approvedOrders - 1,
                    c.shippingOrders = c.shippingOrders + 1
                WHERE c.client.id = :clientId
            """)
    void shippingOrder(String clientId);

    @Modifying
    @Transactional
    @Query("""
                UPDATE ClientStat c
                SET c.shippingOrders = c.shippingOrders - 1,
                    c.completedOrders = c.completedOrders + 1,
                    c.totalSpent = c.totalSpent + :amount
                WHERE c.client.id = :clientId
            """)
    void completeOrder(String clientId, BigDecimal amount);

    @Modifying
    @Transactional
    @Query("""
                UPDATE ClientStat c
                SET c.pendingOrders = c.pendingOrders - 1,
                    c.cancelledOrders = c.cancelledOrders + 1
                WHERE c.client.id = :clientId
            """)
    void cancelOrder(String clientId);

    @Modifying
    @Transactional
    @Query("""
                UPDATE ClientStat c
                SET c.completedOrders = c.completedOrders - 1,
                    c.refundRequestedOrders = c.refundRequestedOrders + 1
                WHERE c.client.id = :clientId
            """)
    void requestRefund(String clientId);

    @Modifying
    @Transactional
    @Query("""
                UPDATE ClientStat c
                SET c.refundRequestedOrders = c.refundRequestedOrders - 1,
                    c.refundedOrders = c.refundedOrders + 1
                WHERE c.client.id = :clientId
            """)
    void refundOrder(String clientId);

    @Modifying
    @Transactional
    @Query("""
                UPDATE ClientStat c
                SET c.shippingOrders = c.shippingOrders - 1,
                    c.failedDeliveryOrders = c.failedDeliveryOrders + 1
                WHERE c.client.id = :clientId
            """)
    void failDelivery(String clientId);

}
