package com.e_cormerce.shoppe.dto.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDTO {
  String id;
  String val;
}
