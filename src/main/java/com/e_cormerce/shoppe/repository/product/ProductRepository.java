package com.e_cormerce.shoppe.repository.product;

import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.user.User;
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
      value = "SELECT * FROM products p  WHERE p.status = 'ACTIVE'  LIMIT :limit OFFSET :offset",
      nativeQuery = true)
  List<Product> findProductForHome(@Param("limit") int limit, @Param("offset") int offset);

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
      value = "SELECT * FROM products p WHERE  p.status = 'ACTIVE' and category_id = :category_id",
      nativeQuery = true)
  List<Product> findProductsInCategory(@Param("category_id") String category_id);

  @Query(
      value =
          "SELECT * FROM products p  WHERE seller_id=:sellerId and  p.status = 'ACTIVE'  LIMIT :limit OFFSET :offset",
      nativeQuery = true)
  List<Product> findProductsOfSeller(String sellerId, int limit, int offset);

  @Query(
      value = "SELECT * FROM products p  WHERE seller_id=:sellerId  LIMIT :limit OFFSET :offset",
      nativeQuery = true)
  List<Product> findProductsOfSellerForSeller(String sellerId, int limit, int offset);

  @Transactional
  @Modifying
  @Query(
      value =
          "update products set total_quantity=total_quantity-:quantityOrdered, total_quantity_sold = total_quantity_sold + :quantityOrdered where id=:productId ",
      nativeQuery = true)
  List<String> updateQuantitySold(@Param("productId") String productId, int quantityOrdered);
}
