package com.e_cormerce.shoppe.repository.product;

import com.e_cormerce.shoppe.entity.product.ProductReviewImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewImageRepository extends JpaRepository<ProductReviewImage, String> {
  @Query(
      value = "SELECT * FROM product_review_images WHERE product_review_id IN (:review_ids)",
      nativeQuery = true)
  List<ProductReviewImage> findImageOfReviewIn(@Param("review_ids") List<String> reviewIds);
}
