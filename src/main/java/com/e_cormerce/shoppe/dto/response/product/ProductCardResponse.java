package com.e_cormerce.shoppe.dto.response.product;

import java.math.BigDecimal;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCardResponse {
  String id;
  String name;
  String thumbnail;
  BigDecimal originPrice;
  float discountPercentage;
  int totalQuantity;
}
