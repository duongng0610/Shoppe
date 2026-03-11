package com.e_cormerce.shoppe.dto.response;

import com.e_cormerce.shoppe.dto.common.ProductDTO;
import com.e_cormerce.shoppe.dto.common.TypeResponse;
import com.e_cormerce.shoppe.dto.common.VariantDetailResponse;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetProductDetailResponse {
  ProductDTO productDTO;
  String description;
  String category_name;
  String shop_name;
  List<VariantDetailResponse> variants;
  List<TypeResponse> types;
}
