package com.e_cormerce.shoppe.dto.response.ghn.shop;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnCreateShopResponse {
  Integer code;
  String message;
  GhnCreateShopDataResponse data;
}
