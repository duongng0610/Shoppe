package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.product.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
  Optional<Product> findById(String id);

  @Query(value = "SELECT * FROM products LIMIT :num OFFSET :offset", nativeQuery = true)
  List<Product> findProductForHome(@Param("limit") int limit, @Param("offset") int offset);
}
