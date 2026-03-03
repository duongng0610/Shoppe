package com.e_cormerce.shoppe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_cormerce.shoppe.entity.order.OrderTrackingLocation;


import org.springframework.stereotype.Repository;

@Repository
public interface OrderTrackingLocationRepository extends JpaRepository<OrderTrackingLocation, String> {
}
