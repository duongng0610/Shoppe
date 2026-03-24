package com.e_cormerce.shoppe.repository.search;

import com.e_cormerce.shoppe.dto.common.search.CategoryProjection;
import com.e_cormerce.shoppe.entity.search.KeyWordCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyWordCategoryRepository extends JpaRepository<KeyWordCategory, String> {
  @Query(
      value =
          "SELECT c.val, c.id "
              + "FROM keyword_categories kc "
              + "WHERE kc.keyword LIKE CONCAT('%', :keyword, '%') "
              + "LIMIT :num",
      nativeQuery = true)
  List<CategoryProjection> getRelatedCategoryNames(
      @Param("keyword") String keyword, @Param("num") int num);
}
