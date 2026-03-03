package com.e_cormerce.shoppe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e_cormerce.shoppe.entity.product.ProductReview;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, String> {
}
