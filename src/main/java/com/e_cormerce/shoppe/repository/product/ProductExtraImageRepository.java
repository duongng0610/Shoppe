package com.e_cormerce.shoppe.repository.product;

import com.e_cormerce.shoppe.entity.product.ProductExtraImage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductExtraImageRepository extends JpaRepository<ProductExtraImage, String> {

  boolean existsByImageId(String imageId);

  @Query(
      """
            SELECT DISTINCT pi
            FROM ProductExtraImage pi
            WHERE pi.product.id = :product_id
            """)
  CompletableFuture<List<ProductExtraImage>> findExtraImagesOfProduct(
      @Param("product_id") String product_id);
}
