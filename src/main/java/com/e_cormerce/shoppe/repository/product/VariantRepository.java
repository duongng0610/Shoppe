package com.e_cormerce.shoppe.repository.product;

import com.e_cormerce.shoppe.entity.product.ShoppingCart;
import com.e_cormerce.shoppe.entity.product.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface VariantRepository extends JpaRepository<Variant, String> {
    @Query(
            """
                    SELECT DISTINCT v
                    FROM Variant v
                    LEFT JOIN FETCH v.variantValues
                    WHERE v.product.id = :product_id
                    """)
    CompletableFuture<List<Variant>> findVariantsOfProduct(@Param("product_id") String product_id);

    Optional<Variant> findById(String variant_id);

    @Query(value = "SELECT p.id, name FROM variants join WHERE client_id = :client_id", nativeQuery = true)
    Optional<ShoppingCart> findByUserId(@Param("client_id") String user_id);
}
