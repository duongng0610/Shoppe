package com.e_cormerce.shoppe.projection.product;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ProductDailyRecentProjection {
    String getProductId();

    LocalDate getDate();

    Long getOrderAmount();

    BigDecimal getTotalRevenue();

    Long getTotalUnitOrderAmount();
}
