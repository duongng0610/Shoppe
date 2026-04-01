package com.e_cormerce.shoppe.enums.notification;

public enum NotificationType {
  ORDER("ORDER"),
  TRANSACTION("TRANSACTION"),
  PRODUCT("PRODUCT"),
  ACCOUNT("ACCOUNT"),
  SYSTEM("SYSTEM");
  ;

  private final String value;

  NotificationType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
