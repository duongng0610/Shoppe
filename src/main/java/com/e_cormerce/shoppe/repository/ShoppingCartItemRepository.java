package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, String> {}
