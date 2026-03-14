package com.e_cormerce.shoppe.dto.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerDTO {
  String id;
  String avatar;
  String username;
}
