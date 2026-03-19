package com.e_cormerce.shoppe.service.product.helper;

import com.e_cormerce.shoppe.dto.common.product.TypeDto;
import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import com.e_cormerce.shoppe.dto.request.product.VariantRequest;
import com.e_cormerce.shoppe.entity.product.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateProductHelper {


    public List<Variant> createDefaultVariant(Product product) {
        List<Variant> variants = new ArrayList<>();
        variants.add(
                Variant.builder()
                        .product(product)
                        .thumbnail(product.getThumbnail())
                        .quantity(product.getTotalQuantity())
                        .price(product.getOriginPrice())
                        .build());
        return variants;
    }

    public List<Type> createType(List<TypeDto> typeDtos, Product product) {

        List<Type> types =
                typeDtos.stream()
                        .map(
                                typeDto -> {
                                    Type type =
                                            Type.builder().product(product).val(typeDto.getName()).build();

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
            Type type = findType(variantAttributeDTO.getTypeName(), product);

            VariantValue variantValue =
                    VariantValue.builder()
                            .value(findTypeValue(variantAttributeDTO.getTypeValue(), type))
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
