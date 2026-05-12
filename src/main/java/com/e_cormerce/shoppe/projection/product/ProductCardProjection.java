package com.e_cormerce.shoppe.projection.product;

import java.math.BigDecimal;

public interface ProductCardProjection {
    String getId();

    String getName();

    String getThumbnail();

    Float getDiscountPercentage();

    BigDecimal getOriginPrice();
    

    Float getRate();

    String getSellerId();

    Long getTotalQuantity();

    Long getTotalQuantitySold();

    Long getTotalOrders();

}
