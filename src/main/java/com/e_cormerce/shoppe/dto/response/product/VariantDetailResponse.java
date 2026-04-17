package com.e_cormerce.shoppe.dto.response.product;

import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantDetailResponse {
  String id;
  BigDecimal price;
  String thumbnail;
  long quantity;
  List<VariantAttributeDto> attributes;
  long soldQuantity;
  LocalDateTime createdAt;
}
