package com.e_cormerce.shoppe.repository.product;

import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, String> {
  @Query(value = "SELECT * FROM shopping_cart_items WHERE id IN :ids ", nativeQuery = true)
  List<ShoppingCartItem> findByIdIn(@Param("ids") List<String> itemIds);
}
