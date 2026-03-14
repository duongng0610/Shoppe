package com.e_cormerce.shoppe.repository.product;

import com.e_cormerce.shoppe.entity.product.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, String> {}
