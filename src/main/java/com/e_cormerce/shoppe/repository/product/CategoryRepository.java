package com.e_cormerce.shoppe.repository.product;

import com.e_cormerce.shoppe.dto.common.search.CategoryProjection;
import com.e_cormerce.shoppe.dto.response.category.CategoryDetailResponse;
import com.e_cormerce.shoppe.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findById(String id);

    boolean existsByVal(String val);

    @Query(value = "SELECT * FROM categories WHERE parent_id IS NULL", nativeQuery = true)
    List<Category> findDefault();

    @Query(value = "SELECT * FROM categories WHERE parent_id = :id ", nativeQuery = true)
    List<Category> findChildren(@Param("id") String id);

    @Query(value = "SELECT id, val FROM categories", nativeQuery = true)
    List<CategoryProjection> findAllCategoryNames();

    @Query(value = "SELECT c1.id, c1.val,c1.thumbnail, c1.deleted, c1.created_at, c2.val " +
            "FROM categories c1 " +
            "JOIN categories c2 on c1.parent_id = c2.id " +
            "WHERE c1.id = :id "
            , nativeQuery = true
    )
    Optional<CategoryDetailResponse> findCategoryDetailsById(@Param("id") String id);


}
