package com.e_cormerce.shoppe.projection;

import java.time.LocalDateTime;

public interface MessageProjection {
  String getId();

  String getContent();

  String getSenderId();

  LocalDateTime getCreatedAt();

  LocalDateTime getUpdatedAt();
}
