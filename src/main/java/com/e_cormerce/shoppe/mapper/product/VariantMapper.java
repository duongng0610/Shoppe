package com.e_cormerce.shoppe.mapper.product;

import com.e_cormerce.shoppe.dto.response.product.VariantDetailResponse;
import com.e_cormerce.shoppe.entity.product.Variant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariantMapper {
  VariantDetailResponse toVariantDetailResponse(Variant variant);
}
