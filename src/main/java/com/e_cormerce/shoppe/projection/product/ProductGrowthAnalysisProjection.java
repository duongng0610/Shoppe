package com.e_cormerce.shoppe.projection.product;

import java.math.BigDecimal;

public interface ProductGrowthAnalysisProjection {
    String getProductId();

    Long getOldOrderAmount();

    Long getNewOrderAmount();

    BigDecimal getOrderGrowthPercent();

    Long getOldUnitOrderAmount();

    Long getNewUnitOrderAmount();

    BigDecimal getUnitGrowthPercent();

    BigDecimal getOldRevenue();

    BigDecimal getNewRevenue();

    BigDecimal getRevenueGrowthPercent();
}
