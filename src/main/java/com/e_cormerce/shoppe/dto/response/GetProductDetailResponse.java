package com.e_cormerce.shoppe.dto.response;

import com.e_cormerce.shoppe.dto.common.*;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetProductDetailResponse {
  String id;
  String description;
  CategoryDTO category;
  SellerDTO seller;
  List<VariantDetailResponse> variants;
  List<TypeResponse> types;
}
