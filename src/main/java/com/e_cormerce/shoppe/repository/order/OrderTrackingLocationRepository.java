package com.e_cormerce.shoppe.repository.order;

import com.e_cormerce.shoppe.entity.order.OrderTrackingLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTrackingLocationRepository
    extends JpaRepository<OrderTrackingLocation, String> {}
