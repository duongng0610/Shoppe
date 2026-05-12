// package com.e_cormerce.shoppe.repository.analytic;
//
// import com.e_cormerce.shoppe.entity.analytic.product.all.ProductDaily;
// import jakarta.transaction.Transactional;
// import java.math.BigDecimal;
// import java.time.LocalDate;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;
//
// @Repository
// public interface ProductDailyRepository extends JpaRepository<ProductDaily, Integer> {
//  @Modifying
//  @Transactional
//  @Query(
//      value =
//          """
//                INSERT INTO product_daily (product_id, date,
//                                           total_views)
//                VALUES (:productId, :date, 1)
//                ON DUPLICATE KEY UPDATE
//                    total_views = total_views + 1
//            """,
//      nativeQuery = true)
//  void increaseView(String productId, LocalDate date);
//
//  @Modifying
//  @Transactional
//  @Query(
//      value =
//          """
//                INSERT INTO product_daily (product_id, date,
//                                           total_orders, total_units_orders)
//                VALUES (:productId, :date, 1, :orderQuantity)
//                ON DUPLICATE KEY UPDATE
//                    total_orders = total_orders + 1,
//                    total_units_orders= total_units_orders + :orderQuantity
//            """,
//      nativeQuery = true)
//  void increaseOrder(String productId, int orderQuantity, LocalDate date);
//
//  @Modifying
//  @Transactional
//  @Query(
//      value =
//          """
//                INSERT INTO product_daily (product_id, date,
//                                           total_revenue)
//                VALUES (:productId, :date, :orderAmount)
//                ON DUPLICATE KEY UPDATE
//                    total_revenue = total_revenue + :orderAmount
//            """,
//      nativeQuery = true)
//  void increaseRevenue(String productId, BigDecimal orderAmount, LocalDate date);
// }
