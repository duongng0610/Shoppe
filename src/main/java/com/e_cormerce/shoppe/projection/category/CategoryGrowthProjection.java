package com.e_cormerce.shoppe.projection.category;

import java.math.BigDecimal;

public interface CategoryGrowthProjection {
    String getCategoryId();

    Integer getOldSearchCount();

    Integer getNewSearchCount();

    Integer getNewNewProductCount();

    Integer getOldNewProductCount();

    BigDecimal getSearchGrowthPercent();

    BigDecimal getNewProductGrowthPercent();
}
