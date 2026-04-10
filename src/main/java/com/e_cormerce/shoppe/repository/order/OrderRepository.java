package com.e_cormerce.shoppe.repository.order;

import com.e_cormerce.shoppe.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findById(String id);

    @Query(value = """
            select client_id 
            from orders 
            where id= :id""", nativeQuery = true)
    Optional<String> getClientId(@Param("id") String id);
}
