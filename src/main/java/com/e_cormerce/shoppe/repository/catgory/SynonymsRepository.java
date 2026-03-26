package com.e_cormerce.shoppe.repository.catgory;

import com.e_cormerce.shoppe.dto.common.search.CategoryDto;
import com.e_cormerce.shoppe.entity.category.CategorySynonyms;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SynonymsRepository extends JpaRepository<CategorySynonyms, String> {
  @Query(
      value =
          "SELECT id, val "
              + "FROM category_synonyms "
              + "WHERE val LIKE CONCAT('%', :keyword, '%') "
              + "LIMIT :num",
      nativeQuery = true)
  List<CategoryDto> getCategorySynonyms(@Param("keyword") String keyword, @Param("num") int num);
}
