package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.product.TypeValue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeValueRepository extends JpaRepository<TypeValue, String> {
  @Query(value = "SELECT * FROM type WHERE product_id = :product_id", nativeQuery = true)
  List<TypeValue> findValueOfType(@Param("type_id") String type_id);
}
