package com.e_cormerce.shoppe.projection.category;

import java.math.BigDecimal;

public interface CategoryStatisticsProjection {
  String getId();

  String getVal();

  String getThumbnail();

  Integer getActiveProductCount();

  Integer getBannedProductCount();

  Integer getHiddenProductCount();

  Integer getPendingProductCount();

  Integer getRejectedProductCount();

  BigDecimal getRevenue();
}
