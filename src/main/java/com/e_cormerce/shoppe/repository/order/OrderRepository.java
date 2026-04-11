package com.e_cormerce.shoppe.repository.order;

import com.e_cormerce.shoppe.entity.order.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
  Optional<Order> findById(String id);

  @Query(value = "SELECT * FROM orders WHERE client_id = :client_id", nativeQuery = true)
  List<Order> findOrderOfClient(@Param("client_id") String clientId);

  @Query(value = "SELECT * FROM orders WHERE seller_id = :seller_id", nativeQuery = true)
  List<Order> findOrderOfSeller(@Param("seller_id") String sellerId);
}
