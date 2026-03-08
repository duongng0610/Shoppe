package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.order.OrderNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderNotificationRepository extends JpaRepository<OrderNotification, String> {}
