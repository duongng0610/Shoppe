package com.e_cormerce.shoppe.enums.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum RoleEnum {
  ADMIN,
  CLIENT,
  SELLER,
  SHIPPER;
}
