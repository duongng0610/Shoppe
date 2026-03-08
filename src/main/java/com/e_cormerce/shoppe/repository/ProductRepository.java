package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {}
