package com.e_cormerce.shoppe.dto.response.order;

import java.math.BigDecimal;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
  String id;
  String productName;
  String thumbnail;
  BigDecimal totalPrice;
  Integer quantity;
}
