package com.e_cormerce.shoppe.repository.shopping_cart;

import com.e_cormerce.shoppe.dto.response.product.BaseProductResponse;
import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, String> {
    @Query(value = "SELECT * FROM shopping_cart_items WHERE id IN :ids ", nativeQuery = true)
    List<ShoppingCartItem> findByIdIn(@Param("ids") List<String> itemIds);

    @Query(value = "SELECT p.id, p.name  FROM shopping_cart_items sci " +
            "join variants v on sci.variant_id = v.id " +
            "join products p on v.product_id = p.id " +
            "WHERE sci.id = :cart_id", nativeQuery = true)
    Optional<BaseProductResponse> findProductByCartItemId(@Param("cart_id") String cartId);
}
