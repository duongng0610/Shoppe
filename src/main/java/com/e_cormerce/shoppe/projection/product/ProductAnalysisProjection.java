package com.e_cormerce.shoppe.projection.product;

import java.math.BigDecimal;

public interface ProductAnalysisProjection {

    String getId();

    String getName();

    String getThumbnail();

    String getStatus();

    Float getRate();

    Boolean getDeleted();

    String getSellerId();

    Long getTotalQuantity();

    Long getTotalQuantitySold();

    Long getTotalOrders();

    BigDecimal getTotalRevenue();
}
