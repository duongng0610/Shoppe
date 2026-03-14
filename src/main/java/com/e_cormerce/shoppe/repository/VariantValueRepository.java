package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.product.VariantValue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantValueRepository extends JpaRepository<VariantValue, String> {
  @Query(value = "SELECT * FROM variant_values WHERE variant_id = :variant_id", nativeQuery = true)
  List<VariantValue> findValueOfVariant(@Param("variant_id") String variant_id);
}
