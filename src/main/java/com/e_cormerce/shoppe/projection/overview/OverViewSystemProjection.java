package com.e_cormerce.shoppe.projection.overview;

public interface OverViewSystemProjection {
  Long getUserCount();

  Long getActiveUserCount();

  Long getBannedUserCount();

  Long getPendingProductCount();

  Long getActiveProductCount();

  Long getBannedProductCount();

  Long getPendingOrderCount();

  Long getApprovedOrderCount();

  Long getShippingOrderCount();

  Long getDeliveredOrderCount();

  Long getFailDeliveryOrderCount();

  Double getTotalRevenue();
}
