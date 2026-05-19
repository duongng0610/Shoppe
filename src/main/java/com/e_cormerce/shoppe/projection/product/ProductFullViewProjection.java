package com.e_cormerce.shoppe.projection.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProductFullViewProjection {
  String getId();

  String getProductName();

  String getDescription();

  String getThumbnail();

  BigDecimal getOriginPrice();

  Float getDiscountPercentage();

  Float getRate();

  String getStatus();

  Integer getTotalQuantity();

  Float getTotalQuantitySold();

  Boolean getHasVariant();

  LocalDateTime getCreatedAt();

  LocalDateTime getUpdatedAt();

  Boolean getDeleted();

  String getSellerId();

  String getSellerUsername();

  String getSellerAvatar();

  String getCategoryId();

  String getCategoryName();

  String getCategoryThumbnail();

  String getCategoryPathToParent();
}
