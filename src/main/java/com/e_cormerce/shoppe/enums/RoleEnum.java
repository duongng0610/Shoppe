package com.e_cormerce.shoppe.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum RoleEnum {
  ADMIN("ADMIN"),
  CLIENT("CLIENT"),
  SELLER("SELLER"),
  SHIPPER("SHIPPER");
  String value;
}
