package com.e_cormerce.shoppe.dto.response.product;

import com.e_cormerce.shoppe.dto.common.catgory.CategoryDto;
import com.e_cormerce.shoppe.dto.common.product.TypeDto;
import com.e_cormerce.shoppe.dto.common.user.UserDto;
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
public class ProductDetailResponse {
  String id;
  String name;
  String description;
  Boolean hasVariant;
  CategoryDto category;
  UserDto seller;
  String thumbnail;
  List<String> extraImages;
  List<VariantDetailResponse> variants;
  List<TypeDto> types;
  Integer totalQuantity;
  Integer totalSoldQuantity;
  BigDecimal originPrice;
  LocalDateTime createdAt;
}
