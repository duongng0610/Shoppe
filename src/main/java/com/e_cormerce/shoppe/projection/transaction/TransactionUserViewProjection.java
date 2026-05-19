package com.e_cormerce.shoppe.projection.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionUserViewProjection {
  String getTransactionId();

  BigDecimal getAmount();

  String getOrderId();

  String getStatus();

  LocalDateTime getCreatedAt();

  String getUserId();

  String getUsername();

  String getAvatar();
}
