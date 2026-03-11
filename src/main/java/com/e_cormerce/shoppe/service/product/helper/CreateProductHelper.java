package com.e_cormerce.shoppe.service.product.helper;

import com.e_cormerce.shoppe.dto.common.*;
import com.e_cormerce.shoppe.entity.product.*;
import com.e_cormerce.shoppe.mapper.product.VariantMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductHelper {

  VariantMapper variantMapper;

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

  public List<Type> createType(List<TypeRequest> typeRequests, Product product) {

    List<Type> types =
        typeRequests.stream()
            .map(
                typeRequest -> {
                  Type type =
                      Type.builder().product(product).val(typeRequest.getTypeName()).build();

                  type.setTypeValues(createTypeValues(typeRequest, type));
                  return type;
                })
            .toList();

    return types;
  }

  private List<TypeValue> createTypeValues(TypeRequest typeRequest, Type type) {
    List<TypeValue> typeValues = new ArrayList<>();

    for (String typeValueVal : typeRequest.getTypeValues()) {
      TypeValue typeValue = TypeValue.builder().val(typeValueVal).type(type).build();

      typeValues.add(typeValue);
    }

    return typeValues;
  }

  public List<Variant> createVariants(
      List<VariantRequest> variantRequests, Product product, List<String> variantImageUrls) {

    List<Variant> variants = new ArrayList<>();
    for (int i = 0; i < variantRequests.size(); i++) {
      variants.add(createVariant(variantRequests.get(i), product, variantImageUrls.get(i)));
    }
    return variants;
  }

  private Variant createVariant(
      VariantRequest variantRequest, Product product, String variantImageUrl) {
    Variant variant =
        Variant.builder()
            .product(product)
            .price(variantRequest.getPrice())
            .quantity(variantRequest.getQuantity())
            .thumbnail(variantImageUrl)
            .build();

    List<VariantValue> variantValues = new ArrayList<>();

    for (VariantValueDTO variantValueDTO : variantRequest.getVariantValues()) {
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

  public List<TypeResponse> createTypesResponse(List<Type> types) {
    List<TypeResponse> typeResponses = new ArrayList<>();
    for (Type type : types) {

      List<TypeValueResponse> typeValueResponses =
          type.getTypeValues().stream()
              .map(
                  typeValue -> {
                    TypeValueResponse typeValueResponse =
                        TypeValueResponse.builder().name(typeValue.getVal()).build();
                    return typeValueResponse;
                  })
              .toList();

      TypeResponse response =
          TypeResponse.builder().name(type.getVal()).typeValues(typeValueResponses).build();
    }

    return typeResponses;
  }

  public List<VariantDetailResponse> createVariantDetail(List<Variant> variants) {
    List<VariantDetailResponse> responses = new ArrayList<>();

    for (Variant variant : variants) {
      List<VariantValueDTO> variantValues =
          variant.getVariantValues().stream()
              .map(
                  variantValue -> {
                    VariantValueDTO variantValueDTO =
                        VariantValueDTO.builder()
                            .typeValue(variantValue.getValue().getVal())
                            .typeName(variantValue.getValue().getType().getVal())
                            .build();

                    return variantValueDTO;
                  })
              .toList();

      VariantDetailResponse response = variantMapper.toVariantDetailResponse(variant);
      response.setVariantValues(variantValues);
    }
    return responses;
  }
}
