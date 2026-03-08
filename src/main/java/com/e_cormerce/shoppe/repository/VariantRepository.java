package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.product.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends JpaRepository<Variant, String> {}
