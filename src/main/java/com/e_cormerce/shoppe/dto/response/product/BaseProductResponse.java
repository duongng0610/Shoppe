package com.e_cormerce.shoppe.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseProductResponse {
  String id;
  String name;
}
