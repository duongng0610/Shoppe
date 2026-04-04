package com.e_cormerce.shoppe.service.product.helper;

import com.e_cormerce.shoppe.dto.common.product.TypeDto;
import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import com.e_cormerce.shoppe.dto.request.product.VariantRequest;
import com.e_cormerce.shoppe.entity.product.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateProductHelper {


    public void addVariant(Variant variant, Product product) {
        var variants = product.getVariants();
        if (variants == null) {
            variants = new ArrayList<>();
        }
        variants.add(variant);
        variant.setProduct(product);
    }
    public void createDefaultVariant(Product product) {
        Variant v = Variant.builder()
                .isDefault(true)
                .thumbnail(product.getThumbnail())
                .quantity(product.getTotalQuantity())
                .price(product.getOriginPrice())
                .build();

        addVariant(v,product); // 💥 QUAN TRỌNG
    }

  public List<Type> createType(List<TypeDto> typeDtos, Product product) {

    List<Type> types =
        typeDtos.stream()
            .map(
                typeDto -> {
                  Type type = Type.builder().product(product).val(typeDto.getName()).build();

                  type.setTypeValues(createTypeValues(typeDto, type));
                  return type;
                })
            .toList();

    return types;
  }

  private List<TypeValue> createTypeValues(TypeDto typeDto, Type type) {
    List<TypeValue> typeValues = new ArrayList<>();

    for (String typeValueVal : typeDto.getValues()) {
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

    for (VariantAttributeDto variantAttributeDTO : variantRequest.getVariantValues()) {
      Type type = findType(variantAttributeDTO.getName(), product);

      VariantValue variantValue =
          VariantValue.builder()
              .value(findTypeValue(variantAttributeDTO.getValue(), type))
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
