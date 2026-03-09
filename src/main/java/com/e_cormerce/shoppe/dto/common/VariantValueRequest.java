package com.e_cormerce.shoppe.dto.common;

import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantValueRequest {
  String typeName;
  String typeValue;
}
