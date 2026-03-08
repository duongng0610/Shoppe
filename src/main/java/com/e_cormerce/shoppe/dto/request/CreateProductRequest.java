package com.e_cormerce.shoppe.dto.request;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {
  String name;
  String reason;
  BigDecimal originPrice;

  boolean hasVariant;
  int totalQuantity;

  List<TypeRequest> types;
  List<VariantRequest> variantRequests;
}
