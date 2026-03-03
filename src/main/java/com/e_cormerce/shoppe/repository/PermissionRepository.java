package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsByVal(String val);

    Permission findByVal(String val);
}
