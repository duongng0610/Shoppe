package com.e_cormerce.shoppe.dto.common;

import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantValueDTO {
  String typeName;
  String typeValue;
}
