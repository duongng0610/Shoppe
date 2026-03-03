package com.e_cormerce.shoppe.repository;


import com.e_cormerce.shoppe.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByVal(String val);
}
