package com.e_cormerce.shoppe.projection.overview;

public interface OverviewProductSystem {
  Long getTotalProductCount();

  Long getPendingProductCount();

  Long getActiveProductCount();

  Long getBannedProductCount();

  Long getHiddenProductCount();
}
