package com.e_cormerce.shoppe.dto.response;

import com.e_cormerce.shoppe.entity.product.Category;
import com.e_cormerce.shoppe.entity.product.Type;
import com.e_cormerce.shoppe.entity.product.Variant;
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
public class CreateProductResponse {
  String name;
  String description;
  BigDecimal originPrice;

  LocalDateTime created_at;
  int total_quantity;

  String category_id;

  List<Type> types;
  List<Variant> variants;
}
