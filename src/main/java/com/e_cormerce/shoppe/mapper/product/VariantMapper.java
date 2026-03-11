package com.e_cormerce.shoppe.mapper.product;

import com.e_cormerce.shoppe.dto.common.VariantDetailResponse;
import com.e_cormerce.shoppe.entity.product.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VariantMapper {
  @Mapping(target = "variantValues", ignore = true)
  @Mapping(target = "discountPercentage", source = "discount_percentage")
  VariantDetailResponse toVariantDetailResponse(Variant variant);
}
