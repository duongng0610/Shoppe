package com.e_cormerce.shoppe.repository.analytic;

import com.e_cormerce.shoppe.entity.analytic.ProductDaily;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ProductDailyRepository extends JpaRepository<ProductDaily, Integer> {
    @Modifying
    @Transactional
    @Query(value = """
                INSERT INTO product_daily (product_id, date, seller_id, category_id,
                                           total_views, total_orders, total_units_sold, total_revenue)
                VALUES (:productId, :date, :sellerId, :categoryId, 1, 0, 0, 0)
                ON DUPLICATE KEY UPDATE
                    total_views = total_views + 1,
                    seller_id = VALUES(seller_id),
                    category_id = VALUES(category_id)
            """, nativeQuery = true)
    void increaseView(String productId, LocalDate date, String sellerId, String categoryId);
}
