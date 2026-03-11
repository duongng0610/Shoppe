package com.e_cormerce.shoppe.service.product.helper;

import com.e_cormerce.shoppe.dto.common.TypeDTO;
import com.e_cormerce.shoppe.dto.common.VariantDTO;
import com.e_cormerce.shoppe.dto.common.VariantValueDTO;
import com.e_cormerce.shoppe.entity.product.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductHelper {

  public List<Variant> createDefaultVariant(Product product) {
    List<Variant> variants = new ArrayList<>();
    variants.add(
        Variant.builder()
            .product(product)
            .thumbnail(product.getThumbnail())
            .quantity(product.getTotal_quantity())
            .price(product.getOriginPrice())
            .build());
    return variants;
  }

  public List<Type> createType(List<TypeDTO> typeDTOS, Product product) {

    List<Type> types =
        typeDTOS.stream()
            .map(
                typeDTO -> {
                  Type type = Type.builder().product(product).val(typeDTO.getTypeName()).build();

                  type.setTypeValues(createTypeValues(typeDTO, type));
                  return type;
                })
            .toList();

    return types;
  }

  private List<TypeValue> createTypeValues(TypeDTO typeDTO, Type type) {
    List<TypeValue> typeValues = new ArrayList<>();

    for (String typeValueVal : typeDTO.getTypeValues()) {
      TypeValue typeValue = TypeValue.builder().val(typeValueVal).type(type).build();

      typeValues.add(typeValue);
    }

    return typeValues;
  }

  public List<Variant> createVariants(
      List<VariantDTO> variantDTOS, Product product, List<String> variantImageUrls) {

    List<Variant> variants = new ArrayList<>();
    for (int i = 0; i < variantDTOS.size(); i++) {
      variants.add(createVariant(variantDTOS.get(i), product, variantImageUrls.get(i)));
    }
    return variants;
  }

  private Variant createVariant(VariantDTO variantDTO, Product product, String variantImageUrl) {
    Variant variant =
        Variant.builder()
            .product(product)
            .price(variantDTO.getPrice())
            .quantity(variantDTO.getQuantity())
            .thumbnail(variantImageUrl)
            .build();

    List<VariantValue> variantValues = new ArrayList<>();

    for (VariantValueDTO variantValueDTO : variantDTO.getVariantValues()) {
      Type type = findType(variantValueDTO.getTypeName(), product);

      VariantValue variantValue =
          VariantValue.builder()
              .value(findTypeValue(variantValueDTO.getTypeValue(), type))
              .variant(variant)
              .build();
      variantValues.add(variantValue);
    }

    variant.setVariantValues(variantValues);
    return variant;
  }

  private Type findType(String typeName, Product product) {
    for (Type type : product.getTypes()) {
      if (type.getVal().equals(typeName)) {
        return type;
      }
    }
    return null;
  }

  private TypeValue findTypeValue(String typeValueVal, Type type) {
    for (TypeValue typeValue : type.getTypeValues()) {
      if (typeValue.getVal().equals(typeValueVal)) {
        return typeValue;
      }
    }
    return null;
  }
}
