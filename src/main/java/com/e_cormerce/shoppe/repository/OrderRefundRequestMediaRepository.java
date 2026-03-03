package com.e_cormerce.shoppe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e_cormerce.shoppe.entity.order.OrderRefundRequestMedia;

@Repository
public interface OrderRefundRequestMediaRepository extends JpaRepository<OrderRefundRequestMedia, String> {
}
