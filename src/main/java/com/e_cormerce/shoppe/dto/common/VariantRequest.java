package com.e_cormerce.shoppe.dto.common;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantRequest {
  List<VariantValueDTO> variantValues;
  BigDecimal price;
  int quantity;
}
