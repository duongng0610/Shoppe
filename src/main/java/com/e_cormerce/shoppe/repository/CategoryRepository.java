package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {}
