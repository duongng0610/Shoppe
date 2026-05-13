package com.e_cormerce.shoppe.repository.product;

import com.e_cormerce.shoppe.entity.product.ProductReview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, String> {
  @Query(
      value =
          "SELECT * FROM product_reviews pr "
              + "WHERE pr.product_id = :product_id "
              + "ORDER BY pr.created_at DESC "
              + "LIMIT :limit OFFSET :offset",
      nativeQuery = true)
  List<ProductReview> findReviewOfProduct(
      @Param("product_id") String productId,
      @Param("limit") int limit,
      @Param("offset") int offset);
}
