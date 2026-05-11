package com.e_cormerce.shoppe.repository.catgory;

import com.e_cormerce.shoppe.dto.response.category.CategoryDetailResponse;
import com.e_cormerce.shoppe.entity.category.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
  Optional<Category> findById(String id);

  Category findByVal(String name);

  boolean existsByVal(String val);

  @Query(value = "SELECT * FROM categories WHERE path_to_parent IS NULL", nativeQuery = true)
  List<Category> findDefault();

  @Query(
      value = "SELECT * FROM categories WHERE SUBSTRING_INDEX(path_to_parent, '/', -1) = :id",
      nativeQuery = true)
  List<Category> findChildren(@Param("id") String id);

  @Query(
      value =
          """
                    SELECT c1.id,
                           c1.val,
                           c1.thumbnail,
                           c1.deleted,
                           c1.created_at as createdAt,
                           c2.val AS parentVal
                    FROM categories c1
                    LEFT JOIN categories c2
                      ON c1.path_to_parent LIKE '%/%'
                     AND c2.id = SUBSTRING_INDEX(c1.path_to_parent, '/', -1)
                    WHERE c1.id = :id
                    """,
      nativeQuery = true)
  Optional<CategoryDetailResponse> findCategoryDetailsById(@Param("id") String id);
}
