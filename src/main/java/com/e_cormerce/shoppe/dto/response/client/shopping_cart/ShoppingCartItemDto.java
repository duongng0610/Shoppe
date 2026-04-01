package com.e_cormerce.shoppe.dto.response.client.shopping_cart;

import com.e_cormerce.shoppe.dto.response.product.BaseProductResponse;
import com.e_cormerce.shoppe.dto.response.product.VariantDetailResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartItemDto {
  String id;
  int quantity;
  boolean isVariant;
  BaseProductResponse product;
  VariantDetailResponse variant;
}
