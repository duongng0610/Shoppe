package com.e_cormerce.shoppe.repository.product;

import com.e_cormerce.shoppe.entity.product.Category;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findById(String id);

    @Query(value = "SELECT * FROM products p  WHERE p.status = 'APPROVED'  LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Product> findProductForHome(@Param("limit") int limit, @Param("offset") int offset);

    @Query(
            value =
                    "SELECT c.* FROM categories c JOIN products p ON p.category_id = c.id WHERE p.id = :product_id",
            nativeQuery = true)
    CompletableFuture<Category> findCategoryOfProduct(@Param("product_id") String productId);

    @Transactional
    @Modifying
//cho phép thay đổi, mặc định @query chỉ là truy vấn select.
    void updateStatus(@Param("ids") List<String> ids, @Param("status") String status);
  @Query(
      value =
          "SELECT u.* FROM users u JOIN products p ON p.seller_id = u.id WHERE p.id = :product_id",
      nativeQuery = true)
  CompletableFuture<User> findSellerOfProduct(@Param("product_id") String productId);

  @Query(value = "SELECT * FROM products WHERE category_id = :category_id", nativeQuery = true)
  List<Product> findProductsInCategory(@Param("category_id") String category_id);
}
