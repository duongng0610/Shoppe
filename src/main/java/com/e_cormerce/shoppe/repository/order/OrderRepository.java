package com.e_cormerce.shoppe.repository.order;

import com.e_cormerce.shoppe.entity.order.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
  Optional<Order> findById(String id);
}
