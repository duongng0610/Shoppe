package com.e_cormerce.shoppe.mapper.product;

import com.e_cormerce.shoppe.dto.common.product.TypeDto;
import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import com.e_cormerce.shoppe.dto.response.product.BaseProductResponse;
import com.e_cormerce.shoppe.dto.response.product.MyProductResponse;
import com.e_cormerce.shoppe.dto.response.product.MyVariantResponse;
import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import com.e_cormerce.shoppe.entity.product.*;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {VariantMapper.class})
public interface ProductMapper {
  ProductCardResponse toProductCardDto(Product product);

  BaseProductResponse toBaseProductDTO(Product product);

  @Mapping(source = "variants", target = "variants")
  MyProductResponse toMyProductDTO(Product product);

  @Mapping(source = "variantValues", target = "attributes")
  MyVariantResponse toMyVariantDto(Variant variant);

  @Mapping(source = "val", target = "name")
  @Mapping(source = "typeValues", target = "values")
  TypeDto toTypeDTO(Type type);

  // custom default
  default List<String> mapTypeValues(List<TypeValue> typeValues) {
    if (typeValues == null) {
      return null;
    }
    return typeValues.stream().filter(tv -> !tv.isDeleted()).map(item -> item.getVal()).toList();
  }

  // custom
  default List<VariantAttributeDto> mapVariantValues(List<VariantValue> variantValues) {
    return variantValues.stream()
        .filter(v -> !v.isDeleted())
        .map(
            v -> {
              var typeValue = v.getValue();
              return VariantAttributeDto.builder()
                  .value(typeValue.getVal())
                  .name(typeValue.getType().getVal())
                  .build();
            })
        .toList();
  }

  default String mapTypeValue(TypeValue typeValue) {
    return typeValue.getType().getVal();
  }
}
