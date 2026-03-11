package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.product.Variant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends JpaRepository<Variant, String> {
  @Query(value = "SELECT * FROM variants WHERE product_id = :product_id", nativeQuery = true)
  CompletableFuture<List<Variant>> findVariantsOfProduct(@Param("product_id") String product_id);
}
