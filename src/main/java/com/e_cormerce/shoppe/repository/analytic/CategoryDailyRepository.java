package com.e_cormerce.shoppe.repository.analytic;

import com.e_cormerce.shoppe.entity.analytic.CategoryDaily;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CategoryDailyRepository extends JpaRepository<CategoryDaily, String> {

    @Modifying
    @Transactional
    @Query(value = """
                INSERT INTO category_daily (category_id, date, category_name, total_searches)
                VALUES (:categoryId, :date, :categoryName, 1)
                ON DUPLICATE KEY UPDATE
                    total_searches = total_searches + 1
            """, nativeQuery = true)
    void increaseSearch(String categoryId, LocalDate date, String categoryName);

    @Modifying
    @Transactional
    @Query(value = """
                INSERT INTO category_daily (category_id, date, category_name, total_new_products)
                VALUES (:categoryId, :date, :categoryName, 1)
                ON DUPLICATE KEY UPDATE
                    total_new_products = total_new_products + 1
            """, nativeQuery = true)
    void increaseNewProduct(String categoryId, LocalDate date, String categoryName);


}
