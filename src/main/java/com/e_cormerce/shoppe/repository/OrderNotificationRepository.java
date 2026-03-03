package com.e_cormerce.shoppe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e_cormerce.shoppe.entity.order.OrderNotification;

@Repository
public interface OrderNotificationRepository extends JpaRepository<OrderNotification, String> {
}
