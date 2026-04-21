package com.e_cormerce.shoppe.repository.user;

import com.e_cormerce.shoppe.entity.user.Permission;
import com.e_cormerce.shoppe.enums.user.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsByVal(String val);

    Optional<Permission> findByVal(PermissionEnum val);
}
