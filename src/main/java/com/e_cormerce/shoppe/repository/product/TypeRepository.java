package com.e_cormerce.shoppe.repository.product;

import com.e_cormerce.shoppe.entity.product.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, String> {
  @Query("""
SELECT DISTINCT t
FROM Type t
WHERE t.product.id = :product_id
""")
  CompletableFuture<List<Type>> findTypeOfProduct(@Param("product_id") String product_id);
}
