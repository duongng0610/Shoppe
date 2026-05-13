package com.e_cormerce.shoppe.repository.product;

import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.projection.product.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
  Optional<Product> findById(String id);

  @Query(
      value = "SELECT * FROM active_products_view LIMIT :limit OFFSET :offset",
      nativeQuery = true)
  List<Product> findProductForHome(@Param("limit") int limit, @Param("offset") int offset);

  @Query(
      value =
          "SELECT p.* FROM orders o "
              + "INNER JOIN variants v ON o.variant_id = v.id "
              + "INNER JOIN products p ON v.product_id = p.id "
              + "WHERE o.id = :order_id",
      nativeQuery = true)
  Product findProductOfOrder(@Param("order_id") String orderId);

  @Query(
      value =
          "SELECT c.* FROM categories c JOIN products p ON p.category_id = c.id WHERE p.id = :product_id",
      nativeQuery = true)
  CompletableFuture<Category> findCategoryOfProduct(@Param("product_id") String productId);

  @Query(value = "UPDATE products p SET p.status = :status WHERE p.id = :id", nativeQuery = true)
  @Transactional
  @Modifying
  // cho phép thay đổi, mặc định @query chỉ là truy vấn select.
  void updateStatus(@Param("id") String id, @Param("status") String status);

  @Query(
      value =
          "SELECT u.* FROM users u JOIN products p ON p.seller_id = u.id WHERE p.id = :product_id",
      nativeQuery = true)
  CompletableFuture<User> findSellerOfProduct(@Param("product_id") String productId);

  @Query(
      value =
          "SELECT * FROM products_by_category_view WHERE category_id = :category_id LIMIT :limit OFFSET :offset",
      nativeQuery = true)
  List<Product> findProductsInCategory(
      @Param("category_id") String category_id,
      @Param("limit") int limit,
      @Param("offset") int offset);

  @Query(
      value =
          "SELECT * FROM seller_active_products_view WHERE seller_id=:sellerId LIMIT :limit OFFSET :offset",
      nativeQuery = true)
  List<Product> findProductsOfSeller(
      @Param("sellerId") String sellerId, @Param("limit") int limit, @Param("offset") int offset);

  @Query(
      value =
          "SELECT * FROM seller_all_products_view WHERE seller_id=:sellerId LIMIT :limit OFFSET :offset",
      nativeQuery = true)
  List<Product> findProductsOfSellerForSeller(
      @Param("sellerId") String sellerId, @Param("limit") int limit, @Param("offset") int offset);

  @Transactional
  @Modifying
  @Query(
      value =
          "update products set total_quantity=total_quantity-:quantityOrdered, total_quantity_sold = total_quantity_sold + :quantityOrdered where id=:productId ",
      nativeQuery = true)
  List<String> updateQuantitySold(@Param("productId") String productId, int quantityOrdered);

  @Query(
      value =
          """
                    SELECT *
                    FROM product_analysis_view
                      LIMIT :limit OFFSET :offset
                    """,
      nativeQuery = true)
  List<ProductAnalysisProjection> getAllProductsForAdmin(int limit, int offset);

  @Query(
      value =
          """
                    SELECT *
                    FROM product_analysis_view
                    WHERE sellerId = :sellerId
                     LIMIT :limit OFFSET :offset
                    """,
      nativeQuery = true)
  List<ProductAnalysisProjection> getProductsOfSeller(
      @Param("sellerId") String sellerId, int limit, int offset);

  // admin
  @Query(
      value =
          """
                    CALL get_top_selling_products_recent(
                        NULL,
                        :limit,
                        :offset,
                        :days
                    )
                    """,
      nativeQuery = true)
  List<ProductCardProjection> getTopSellingProducts(
      @Param("limit") int limit, @Param("offset") int offset, @Param("days") Integer days);

  // seller
  @Query(
      value =
          """
                    CALL get_top_selling_products_recent(
                        :sellerId,
                        :limit,
                        :offset,
                        :days
                    )
                    """,
      nativeQuery = true)
  List<ProductCardProjection> getTopSellingProductsOfSeller(
      @Param("sellerId") String sellerId,
      @Param("limit") int limit,
      @Param("offset") int offset,
      @Param("days") Integer days);

  @Query(
      value =
          """
                    CALL get_product_daily_recent(
                        :productId,
                        :days
                    )
                    """,
      nativeQuery = true)
  List<ProductDailyRecentProjection> getProductDailyRecent(
      @Param("productId") String productId, @Param("days") Integer days);

  @Query(
      value =
          """
                    CALL analyze_product_growth_full(
                        :productId,
                        :days
                    )
                    """,
      nativeQuery = true)
  ProductGrowthAnalysisProjection analyzeProductGrowth(
      @Param("productId") String productId, @Param("days") Integer days);

  // =====================================
  // PRODUCT FULL VIEW
  // =====================================

  @Query(
      value =
          """
                    SELECT *
                    FROM product_with_seller_and_category
                    LIMIT :offset, :limit
                    """,
      nativeQuery = true)
  List<ProductFullViewProjection> getProductFullViews(
      @Param("limit") Integer limit, @Param("offset") Integer offset);
}
