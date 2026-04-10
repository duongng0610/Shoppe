package com.e_cormerce.shoppe.enums.order;

import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;

public enum OrderStatus {
  PENDING,
  ACCEPTED,
  CANCELLED_BY_CLIENT,
  CANCELLED_BY_SELLER,
  SHIPPING,
  ARRIVED,
  PAID,
  RETURNED,
  BOMB;

  public static OrderStatus fromString(String status) {
    if (status == null || status.trim().isEmpty()) {
      throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
    }
    try {
      return OrderStatus.valueOf(status.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
    }
  }
}
