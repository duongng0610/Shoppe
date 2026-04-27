package com.e_cormerce.shoppe.dto.request.order;

import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderProductInfo {
  @NotBlank(message = "variant id is required")
  String variantId;

  @NotBlank(message = "product id is required")
  String productId;

  List<VariantAttributeDto> attributes;
}
