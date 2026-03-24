package com.e_cormerce.shoppe.repository.search;

import com.e_cormerce.shoppe.dto.common.search.CategoryProjection;
import com.e_cormerce.shoppe.entity.search.KeyWordCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyWordCategoryRepository extends JpaRepository<KeyWordCategory, String> {
    @Query(
            value =
                    "SELECT keyword, id "
                            + "FROM keyword_categories "
                            + "WHERE keyword LIKE CONCAT('%', :keyword, '%') "
                            + "LIMIT :num",
            nativeQuery = true)
    List<CategoryProjection> getRelatedCategoryNames(
            @Param("keyword") String keyword, @Param("num") int num);
}
