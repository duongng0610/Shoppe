package com.e_cormerce.shoppe.util.constants;

import static com.e_cormerce.shoppe.enums.user.RoleEnum.*;

import com.e_cormerce.shoppe.enums.user.PermissionEnum;
import com.e_cormerce.shoppe.enums.user.RoleEnum;
import java.util.Map;
import java.util.Set;

public class RoleValue {
  public static final Map<RoleEnum, Set<PermissionEnum>> ROLE_PERMISSIONS =
      Map.of(
          ADMIN,
          PermissionValue.ADMIN_PERMISSIONS,
          CLIENT,
          PermissionValue.CLIENT_PERMISSIONS,
          SELLER,
          PermissionValue.SELLER_PERMISSIONS);
}
