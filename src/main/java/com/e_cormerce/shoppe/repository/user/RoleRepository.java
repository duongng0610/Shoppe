package com.e_cormerce.shoppe.repository.user;

import com.e_cormerce.shoppe.entity.user.Role;
import com.e_cormerce.shoppe.enums.user.RoleEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
  @Query(
      value =
          "select p.val from role r join role_permission rp on r.id = rp.role_id join permissions p on rp.permission_id=p.id where r.id= :roleId",
      nativeQuery = true)
  List<String> getPermissionsByRole(String roleId);

  boolean existsByVal(String val);

  Optional<Role> findByVal(RoleEnum val);
}
