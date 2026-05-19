package com.e_cormerce.shoppe.projection.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProductCardProjection {
  String getId();

  LocalDateTime getCreatedAt();

  Float getDiscountPercentage();

  String getName();

  BigDecimal getOriginPrice();

  Float getRate();

  String getStatus();

  String getThumbnail();

  Integer getTotalQuantity();

  Float getTotalQuantitySold();

  String getSellerId();

  String getCategoryId();
}
