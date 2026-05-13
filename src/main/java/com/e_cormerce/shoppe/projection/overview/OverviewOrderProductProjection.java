package com.e_cormerce.shoppe.projection.overview;

public interface OverviewOrderProductProjection {
  String getSellerId();

  Long getPendingProductCount();

  Long getActiveProductCount();

  Long getBannedProductCount();

  Long getHiddenProductCount();

  Long getPendingOrderCount();

  Long getApprovedOrderCount();

  Long getShippingOrderCount();

  Long getDeliveriedOrderCount();

  Long getCancelledOrderCount();
}
