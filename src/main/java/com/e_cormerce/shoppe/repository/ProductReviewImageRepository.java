package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.product.ProductReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewImageRepository extends JpaRepository<ProductReviewImage, String> {}
