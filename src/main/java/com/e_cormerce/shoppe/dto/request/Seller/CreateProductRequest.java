package com.e_cormerce.shoppe.dto.request.Seller;

import com.e_cormerce.shoppe.dto.common.TypeDTO;
import com.e_cormerce.shoppe.dto.common.VariantDTO;
import jakarta.validation.constraints.NotNull;
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
  @NotNull String name;
  @NotNull String description;
  @NotNull BigDecimal originPrice;

  boolean hasVariant;
  int totalQuantity;

  String category_id;

  List<TypeDTO> types;
  List<VariantDTO> variantDTOS;
}
