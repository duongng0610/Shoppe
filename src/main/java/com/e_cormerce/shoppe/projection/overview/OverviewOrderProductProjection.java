package com.e_cormerce.shoppe.projection.overview;

import java.math.BigDecimal;

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

    BigDecimal getTotalRevenue();
}
