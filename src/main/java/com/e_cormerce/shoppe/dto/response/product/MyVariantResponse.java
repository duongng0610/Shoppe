package com.e_cormerce.shoppe.dto.response.product;

import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MyVariantResponse {
  String id;
  BigDecimal price;
  String thumbnail;
  Long quantity;
  List<VariantAttributeDto> attributes;
  Long soldQuantity;
  Boolean deleted;
  LocalDateTime createdAt;
}
