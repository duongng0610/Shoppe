package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.product.VariantValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantValueRepository extends JpaRepository<VariantValue, String> {}
