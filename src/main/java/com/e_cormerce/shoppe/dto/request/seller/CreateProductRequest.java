package com.e_cormerce.shoppe.dto.request.seller;

import com.e_cormerce.shoppe.dto.common.TypeRequest;
import com.e_cormerce.shoppe.dto.common.VariantRequest;
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

  @NotNull boolean hasVariant;

  @NotNull int totalQuantity;

  @NotNull String categoryId;

  List<TypeRequest> types;
  List<VariantRequest> variantRequests;
}
