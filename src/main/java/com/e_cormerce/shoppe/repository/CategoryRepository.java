package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.product.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
  Optional<Category> findById(String id);

  boolean existsByVal(String val);

  @Query(value = "SELECT * FROM categories WHERE parent_id IS NULL", nativeQuery = true)
  List<Category> findDefault();

  @Query(value = "SELECT * FROM categories WHERE parent_id = :id ", nativeQuery = true)
  List<Category> findChildren(@Param("id") String id);
}
