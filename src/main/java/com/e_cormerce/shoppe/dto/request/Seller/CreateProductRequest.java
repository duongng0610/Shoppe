package com.e_cormerce.shoppe.dto.request.Seller;

import java.math.BigDecimal;
import java.util.List;

import com.e_cormerce.shoppe.dto.common.TypeRequest;
import com.e_cormerce.shoppe.dto.common.VariantRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {
    @NotNull String name;
    @NotNull String description;
    @NotNull BigDecimal originPrice;

  boolean hasVariant;
  int totalQuantity;

  String category_id;

  List<TypeRequest> types;
  List<VariantRequest> variantRequests;
}
