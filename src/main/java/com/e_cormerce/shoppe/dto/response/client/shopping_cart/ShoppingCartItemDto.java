package com.e_cormerce.shoppe.dto.response.client.shopping_cart;

import com.e_cormerce.shoppe.dto.response.product.VariantDetailResponse;
import java.math.BigDecimal;
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
  BigDecimal priceEach;
  boolean isVariant;
  VariantDetailResponse variant;
}
