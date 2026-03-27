package com.e_cormerce.shoppe.dto.response.client.shopping_cart;

import com.e_cormerce.shoppe.dto.response.product.VariantDetailResponse;

import java.math.BigDecimal;

public class ShoppingCartItemDto {
    String id;


    int quantity;


    BigDecimal priceEach;

    boolean isVariant;


    VariantDetailResponse variant;
}
