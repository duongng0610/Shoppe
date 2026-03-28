package com.e_cormerce.shoppe.repository.shopping_cart;

import com.e_cormerce.shoppe.entity.product.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {
    @Query(value = "SELECT * FROM shopping_carts WHERE client_id = :client_id", nativeQuery = true)
    Optional<ShoppingCart> findByUserId(@Param("client_id") String user_id);


}
