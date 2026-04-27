package com.e_cormerce.shoppe.repository.analytic;

import com.e_cormerce.shoppe.entity.analytic.product.variant.ProductVariantDaily;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantDailyRepository extends JpaRepository<ProductVariantDaily, Integer> {

  @Modifying
  @Transactional
  @Query(
      value =
          """
                INSERT INTO product_variant_daily (variant_id,product_id, date,
                                           total_orders, total_units_orders)
                VALUES (:variantId,:productId, :date, 1, :orderQuantity)
                ON DUPLICATE KEY UPDATE
                    total_orders = total_orders + 1,
                    total_units_orders= total_units_orders + :orderQuantity
            """,
      nativeQuery = true)
  void increaseOrder(String variantId, String productId, int orderQuantity, LocalDate date);

  @Modifying
  @Transactional
  @Query(
      value =
          """
                INSERT INTO product_variant_daily (variant_id,product_id, date,
                                           total_revenue)
                VALUES (:variantId,:productId, :date, :orderAmount)
                ON DUPLICATE KEY UPDATE
                    total_revenue = total_revenue + :orderAmount
            """,
      nativeQuery = true)
  void increaseRevenue(String variantId, String productId, BigDecimal orderAmount, LocalDate date);
}
